package com.korea.news.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korea.news.dto.NewsDTO;
import com.korea.news.service.CommentService;
import com.korea.news.service.LoginService;
import com.korea.news.service.NewsService;
import com.korea.news.service.SubscriptionService;
import com.korea.news.util.TimeUtils;
import com.korea.news.vo.Channel;
import com.korea.news.vo.DaegulVO;
import com.korea.news.vo.UserVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

	private final SubscriptionService subscriptionService;
	private final NewsService newsService;
	private final LoginService loginService;
	private final CommentService commentService;
	private final HttpSession session;
	private final HttpServletRequest request;


    @GetMapping("/home")
    public String home(Model model) {
    	// 사용자 ID를 세션에서 가져와 모델에 추가
        String userId = (String) session.getAttribute("userId");
        System.out.println("User ID from session: " + userId);
        
        // userIdx를 Integer 타입으로 가져오기
        Integer userIdx = null;

        // 로그인이 되어있을 때만 userIdx를 가져옴
        if (userId != null) {
            userIdx = (Integer) session.getAttribute("userIdx");
        }

        System.out.println("User Index from session: " + userIdx);
        
        model.addAttribute("userId", userId);
        model.addAttribute("userIdx",userIdx);

         model.addAttribute("politics", getAndSaveNewsSection("politics", 7));
         model.addAttribute("economy", getAndSaveNewsSection("economy", 6));
         model.addAttribute("society", getAndSaveNewsSection("society", 6));
         model.addAttribute("culture", getAndSaveNewsSection("culture", 6));
         model.addAttribute("entertainment", getAndSaveNewsSection("entertainment", 6));

         TimeUtils.CurrentDate(model);

         List<Channel> channels = Arrays.asList(
                 new Channel("JTBC", "JTBCNEWS.png"),
                 new Channel("KBS", "KBSNEWS.png"),
                 new Channel("MBC", "MBCNEWS.png"),
                 new Channel("SBS", "SBSNEWS.jpg"),
                 new Channel("YTN", "ytn.png"),
                 new Channel("연합뉴스", "연합뉴스.png"),
                 new Channel("연합뉴스TV", "연합뉴스TV.jpg"),
                 new Channel("한국경제TV", "한국경제TV.png")
             );
             model.addAttribute("channels", channels);
             
             if (userIdx != null) {
                 // 구독된 채널 목록을 가져옴
                 List<String> subscribedChannelNames = subscriptionService.getSubscribedChannels(userIdx);
                 
                 // 각 구독된 채널에 대한 뉴스 항목을 포함한 채널 정보 생성
                 List<Channel> subscribedChannels = subscribedChannelNames.stream()
                     .map(channelName -> {
                         Channel channel = channels.stream()
                             .filter(c -> c.getName().equals(channelName))
                             .findFirst()
                             .orElse(null);
                         
                         if (channel != null) {
                             // 해당 채널의 관련 뉴스 가져오기
                             List<NewsDTO> relatedNews = newsService.fetchPublisher(channelName).stream()
                                 .filter(news -> news.getImageUrl() != null && !news.getImageUrl().isEmpty())
                                 .limit(6) // 최대 6개의 뉴스만 가져오기
                                 .collect(Collectors.toList());
                             
                             // 뉴스가 있는 경우 저장
                             channel.setRelatedNews(relatedNews);
                             
                             // 뉴스 DB에 저장
                             newsService.saveAllNews(relatedNews, channelName);
                         }
                         
                         return channel;
                     })
                     .filter(channel -> channel != null)
                     .collect(Collectors.toList());

                 // 구독된 채널 정보를 모델에 추가
                 model.addAttribute("subscribedChannels", subscribedChannels);
             }
    
             if (userIdx != null) {
                 List<String> subscribedChannelNames = subscriptionService.getSubscribedChannels(userIdx);
                 model.addAttribute("subscribedChannelNames", subscribedChannelNames);
             }
         return "news/home";    
     }
    @PostMapping("/unsubscribe")
    @ResponseBody
    public Map<String, Object> unsubscribe(@RequestParam("channelName") String channelName) {
        Map<String, Object> response = new HashMap<>();
        String userId = (String) session.getAttribute("userId");

        if (userId != null) {
            // 세션에서 userIdx를 가져옴
            Integer userIdx = (Integer) session.getAttribute("userIdx");

            if (userIdx == null) {
                // 세션에 userIdx가 없을 경우 처리
                return Map.of("error", "User ID not found in session");
            }

            // 사용자가 채널 구독을 취소
            subscriptionService.unsubscribeFromChannel(userIdx, channelName);

            response.put("status", "unsubscribed");
        } else {
            // 세션에 userId가 없으면 로그인 페이지로 리다이렉트
            response.put("error", "User not logged in");
        }

        return response;
    }

    @PostMapping("/subscribe")
    @ResponseBody
    public Map<String, Object> subscribe(@RequestParam("channelName") String channelName) {
        Map<String, Object> response = new HashMap<>();
        String userId = (String) session.getAttribute("userId");

        if (userId != null) {
            // 세션에서 userIdx를 가져옴
            Integer userIdx = (Integer) session.getAttribute("userIdx");

            if (userIdx == null) {
                // 세션에 userIdx가 없을 경우 처리
                return Map.of("error", "User ID not found in session");
            }

            // 사용자가 채널을 구독
            subscriptionService.subscribeToChannel(userIdx, channelName);

            // 'publisher' 값을 사용하여 뉴스 데이터 가져오기 또는 구독 처리
            List<NewsDTO> newsList = newsService.fetchPublisher(channelName).stream()
                .filter(news -> news.getImageUrl() != null && !news.getImageUrl().isEmpty())
                .collect(Collectors.toList());

            // 필요한 경우, 다른 로직을 추가해 구독 상태를 처리
            response.put("news", newsList.size() > 6 ? newsList.subList(0, 6) : newsList);
            newsService.saveAllNews(newsList, channelName);
        } else {
            // 세션에 userId가 없으면 로그인 페이지로 리다이렉트
            response.put("error", "User not logged in");
        }

        return response;
    }
    


    private List<NewsDTO> getAndSaveNewsSection(String section, int limit) {
        List<NewsDTO> news = newsService.fetchNews(section).stream()
                .filter(newsItem -> newsItem.getImageUrl() != null && !newsItem.getImageUrl().isEmpty())
                .collect(Collectors.toList());
        List<NewsDTO> selectedNews = news.size() > limit ? news.subList(0, limit) : news;
        newsService.saveAllNews(selectedNews, section);
        return selectedNews;
    }
    	
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String newsId, Model model) {
        // 사용자 ID를 세션에서 가져와 모델에 추가
        String userId = (String) session.getAttribute("userId");
        System.out.println("User ID from session: " + userId);  
        model.addAttribute("userId", userId);

        // 1. newsId로 뉴스 정보 가져오기
        NewsDTO news = newsService.findById(newsId);
        model.addAttribute("news", news);
        
        // 댓글 목록 가져오기
        List<DaegulVO> comments = commentService.findCommentsByNewsId(newsId);
        model.addAttribute("comments", comments);
        
        // 2. 뉴스의 sections 가져오기
        String sections = newsService.findsectionsById(newsId);
        
        // 3. 키워드로 관련 뉴스 가져오기 (외부 API 호출)
        List<NewsDTO> recommendedNews = newsService.fetchNews(sections).stream()
                .filter(n -> n.getImageUrl() != null && !n.getImageUrl().isEmpty())
                .collect(Collectors.toList());

        // 추천 뉴스 중 최대 10개 선택
        List<NewsDTO> selectedRecommendedNews = recommendedNews.size() > 10 ? recommendedNews.subList(0, 10) : recommendedNews;

        // 4. 추천 뉴스 저장
        newsService.saveAllNews(selectedRecommendedNews, sections);
        
        // 5. 추천 뉴스를 모델에 추가
        model.addAttribute("relatedNews", selectedRecommendedNews);
        
        // 6. 현재 날짜를 모델에 추가
        TimeUtils.CurrentDate(model);

        return "news/detail";
    }
   // 댓글 작성
    @PostMapping("/{id}/comment")
    public RedirectView postComment(@PathVariable("id") String newsId, DaegulVO vo) {

        String userId = (String) session.getAttribute("userId");
        String ip = (String) session.getAttribute("userIp");
        
        if (userId == null) {
            // 로그인하지 않은 상태라면 로그인 페이지로 리다이렉트
            return new RedirectView("/news/login");
        }
        
        vo.setUserid(userId);
        vo.setIp(ip);

        int res = commentService.insert(vo);
        return new RedirectView("/news/detail/" + newsId);
    }
    //대댓글 작성
    @PostMapping("/{id}/reply")
    public RedirectView postReply(@PathVariable("id") String newsId,
                                  DaegulVO vo,
                                  @RequestParam("num") int num) {
        String userId = (String) session.getAttribute("userId");
        String ip = (String) session.getAttribute("userIp");

        if (userId == null) {
            // 로그인하지 않은 상태라면 로그인 페이지로 리다이렉트
            return new RedirectView("/news/login");
        }
        
        DaegulVO base_vo = commentService.selectOne(num);
        int res = commentService.update_step(base_vo);

        vo.setUserid(userId);
        vo.setIp(ip);
        vo.setRef(base_vo.getRef());
        vo.setStep(base_vo.getStep() + 1);
        vo.setDepth(base_vo.getDepth() + 1);

        res = commentService.reply(vo);
        return new RedirectView("/news/detail/" + newsId);
    }


	//로그인화면
  	@GetMapping("/login")
  	 public String login(@ModelAttribute("vo") UserVO vo, Model model) {
  		TimeUtils.CurrentDate(model);
  	     return "news/login"; 
  	 }
  	
  	@PostMapping("/access")
	@ResponseBody
	public String login(@RequestBody String body) {
		ObjectMapper om = new ObjectMapper();
		Map<String, String> data =null;
		try {
			data = om.readValue(body, new TypeReference<Map<String, String>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String userid = data.get("userid");
		String pwd = data.get("pwd");
		
		UserVO vo = loginService.loginCheck(userid);
		
		if(vo == null || !vo.getPwd().equals(pwd)) {
			return "{\"param\":\"no\"}";
		}
		
		session.setAttribute("userIdx", vo.getIdx());
		session.setAttribute("userId", vo.getUserid());
		session.setAttribute("userIp", request.getRemoteAddr()); 
		return "{\"param\":\"yes\"}";
	}
  	
  	//로그아웃
  	@GetMapping("/logout")
	public RedirectView logout() {
		session.removeAttribute("userId");
		return new RedirectView("/news/home");
	}
  	
  	//회원가입화면
  	 @GetMapping("/register")
  	 public String register(@ModelAttribute("vo") UserVO vo, Model model) {
  		TimeUtils.CurrentDate(model);
  	     return "news/register"; 
  	  }
  	 
  	@PostMapping("/check_id")
	@ResponseBody
	public String check_id(@RequestBody String userid) {
		ObjectMapper om = new ObjectMapper();
		Map<String, String> data =null;
		try {
			data = om.readValue(userid, new TypeReference<Map<String, String>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
		userid = data.get("userid");
	
		UserVO vo = loginService.idCheck(userid);

		if(vo==null) {
			return "{\"param\":\"yes\"}";
		}else {
			return "{\"param\":\"no\"}";
		}
	}
  	
  	@PostMapping("/join")
	public RedirectView join(UserVO vo) {
		int res = loginService.insert(vo);
		if(res > 0) {
			return new RedirectView("/news/login");
		}
		return null;
	}
  	
  	@PostMapping("/edit")
  	@ResponseBody
  	public Map<String, String> editComment(@RequestBody Map<String, String> data) {
  	    Map<String, String> response = new HashMap<>();
  	    try {
  	        int intNum = Integer.parseInt(data.get("num"));
  	        String updatedContent = data.get("content");

  	        // 현재 로그인한 사용자의 ID를 가져옴
  	      String userId = (String) session.getAttribute("userId");

  	        // 수정할 댓글 객체를 조회
  	        DaegulVO vo = commentService.selectOne(intNum);

  	        if (vo != null) {
  	            // 로그인한 사용자와 댓글 작성자가 같은지 확인
  	            if (vo.getUserid().equals(userId)) {
  	                // 댓글 내용 수정
  	                vo.setContent(updatedContent);
  	                int result = commentService.updateComment(vo); // updateComment 메서드로 DB에 업데이트

  	                if (result > 0) {
  	                    response.put("param", "success");
  	                } else {
  	                    response.put("param", "fail");
  	                }
  	            } 
  	        } 
  	    } catch (Exception e) {
  	        e.printStackTrace();
  	        response.put("param", "error");
  	    }
  	    return response;
  	}


  	
  	//삭제
  	@PostMapping("/delete")
  	@ResponseBody
  	public Map<String, String> delete(@RequestBody Map<String, String> data) {
  	    Map<String, String> response = new HashMap<>();
  	    try {
  	        int num = Integer.parseInt(data.get("num"));
  	        DaegulVO vo = commentService.selectOne(num);
  	      String userId = (String) session.getAttribute("userId");
  	       
  	        
  	      if (vo != null) {
	            // 로그인한 사용자와 댓글 작성자가 같은지 확인
	            if (vo.getUserid().equals(userId)) {
	           
	                int result = commentService.delete(vo);

	                if (result > 0) {
	                    response.put("param", "success");
	                } else {
	                    response.put("param", "fail");
	                }
	            } 
	        } 
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("param", "error");
	    }
	    return response;
	}



  	 
  	// 날씨 화면
  	@GetMapping("/weather")
  	public String weather(Model model) {
  		// 사용자 ID를 세션에서 가져와 모델에 추가
        String userId = (String) session.getAttribute("userId");
        model.addAttribute("userId", userId);

  		TimeUtils.CurrentDate(model);
  	    return "weather/weather";
  	}
}
