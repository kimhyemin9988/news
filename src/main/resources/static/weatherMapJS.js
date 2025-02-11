/* api key */

const apiKey = 'onln1BmvLIlFVm327173TOIgaWX%2BF5okicZQ6k0NeMNuIBU0yqyXrW3imG38ZZi9sggZ4a%2FsrSpNsdV15%2FnjjQ%3D%3D';
const url_UltraSrtNcst = 'http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst'; /*초단기 실황 조회 URL*/

/**** 오늘 날짜 불러오기 ****/
const fullDate = new Date();
const alertDayLenth = (number) => {
  let dayNumber = JSON.stringify(number);
  if (dayNumber.length === 1) {
    dayNumber = 0 + dayNumber;
  }
  return dayNumber;
}

const year = JSON.stringify(fullDate.getFullYear());
const month = alertDayLenth(fullDate.getMonth() +1);
const day = alertDayLenth(fullDate.getDate());
const full_date = [year, month, day].join("");	       /*202402121600 */
const twoHourAgo = [Number(fullDate.getHours())-2,'00'].join("");  /* 한시간 전까지는 data가 뜨지 않음 */

/* 현재 지역 날씨 */
const regionCurrent = {region:'부평구 부평동',x:'55',y:'125'};



/*
초단기실황	T1H	기온	℃	10
	RN1	1시간 강수량	mm	8
	UUU	동서바람성분(풍속 동서)	m/s	12
	VVV	남북바람성분(픙속 남북)	m/s	12
	REH	습도	%	8
	VEC	풍향	deg	10
	WSD	풍속	m/s	10
	PTY	강수형태
	->(단기) 맑음(0), 비(1), 비/눈(2), 눈(3), 소나기(4) 
	TMN	일 최저기온	℃	10
	TMX	일 최고기온	℃	10

*/

/*
서울특별시			60	127
부산광역시			98	76
대구광역시			89	90
광주광역시			58	74
대전광역시			67	100
울산광역시			102	84
제주특별자치도		52	38


춘천시		73	134
청주시 		69	106
강릉시		92	131
수원시 		60	121
안동시		91	106
전주시		63	89
여수시		73	66
목포시		50	67


초단기실황	T1H	기온	℃	10
	RN1	1시간 강수량	mm	8
	UUU	동서바람성분	m/s	12
	VVV	남북바람성분	m/s	12
	REH	습도	%	8
	VEC	풍향	deg	10
	WSD	풍속	m/s	10
	PTY	강수형태
	->(단기) 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4) 

*/
	const regionXY = [{region: '서울', x:'60', y:'127'},
	{region: '부산', x:'98', y:'76'},
	{region: '대구', x:'89', y:'90'},
	{region: '서울', x:'60', y:'127'},
	{region: '광주', x:'58', y:'74'},
	{region: '대전', x:'67', y:'100'},
	{region: '울산', x:'102', y:'84'},
	{region: '제주', x:'52', y:'38'},
	{region: '춘천', x:'73', y:'134'},
	{region: '청주', x:'69', y:'106'},
	{region: '강릉', x:'92', y:'131'},
	{region: '수원', x:'60', y:'121'},
	{region: '안동', x:'91', y:'106'},
	{region: '전주', x:'63', y:'89'},
	{region: '여수', x:'73', y:'66'},
	{region: '목포', x:'50', y:'67'},
	]


	/* 초단기 실황 조회 (지역)*/


	var queryParams_UltraSrtNcst = '?' + encodeURIComponent('serviceKey') + '='+apiKey; /*Service Key*/
	queryParams_UltraSrtNcst += '&' + encodeURIComponent('pageNo') + '=' + encodeURIComponent('1'); /**/
	queryParams_UltraSrtNcst += '&' + encodeURIComponent('numOfRows') + '=' + encodeURIComponent('100'); /**/
	queryParams_UltraSrtNcst += '&' + encodeURIComponent('dataType') + '=' + encodeURIComponent('JSON'); /**/
	queryParams_UltraSrtNcst += '&' + encodeURIComponent('base_date') + '=' + encodeURIComponent(full_date); /**/
	queryParams_UltraSrtNcst += '&' + encodeURIComponent('base_time') + '=' + encodeURIComponent(twoHourAgo); /**/
	queryParams_UltraSrtNcst += '&' + encodeURIComponent('nx') + '=' + encodeURIComponent('55'); /**/
	queryParams_UltraSrtNcst += '&' + encodeURIComponent('ny') + '=' + encodeURIComponent('127'); /**/

	//let dataArray = [];
	//let dataArrayRe = [];
	
	/*
	fetch(url_UltraSrtNcst + queryParams_UltraSrtNcst, {
	}).then((response) => response.json())
	.then((data) =>
		{
			dataArray = [...(data.response.body.items.item)];
			dataArrayRe = [{'x' : dataArray[0].nx}, {'y' : dataArray[0].ny}];
			dataArray.forEach((i)=> 
			 dataArrayRe = [...dataArrayRe, i.category, i.obsrValue]); // 좌표포함 data
				//console.log(dataArrayRe);
		}
	);
초단기실황	T1H	기온	℃	10
	RN1	1시간 강수량	mm	8
	UUU	동서바람성분(풍속 동서)	m/s	12
	VVV	남북바람성분(픙속 남북)	m/s	12
	REH	습도	%	8
	VEC	풍향	deg	10
	WSD	풍속	m/s	10
	PTY	강수형태
	->(단기) 맑음(0), 비(1), 비/눈(2), 눈(3), 소나기(4) 
	TMN	일 최저기온	℃	10
	TMX	일 최고기온	℃	10

*/


