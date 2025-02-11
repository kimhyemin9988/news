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
const full_date = [year, month, day].join("");	/*202402121600 */
const twoHourAgo = [Number(fullDate.getHours())-2,'00'].join("");

/* 단기 예보 조회 */

var url_Vilage = 'http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst'; /*URL*/
var apiKey = 'onln1BmvLIlFVm327173TOIgaWX%2BF5okicZQ6k0NeMNuIBU0yqyXrW3imG38ZZi9sggZ4a%2FsrSpNsdV15%2FnjjQ%3D%3D';

var queryParams_Vilage = '?' + encodeURIComponent('serviceKey') + '='+apiKey; /*Service Key*/
queryParams_Vilage += '&' + encodeURIComponent('pageNo') + '=' + encodeURIComponent('1'); /**/
queryParams_Vilage += '&' + encodeURIComponent('numOfRows') + '=' + encodeURIComponent('100'); /**/
queryParams_Vilage += '&' + encodeURIComponent('dataType') + '=' + encodeURIComponent('JSON'); /**/
queryParams_Vilage += '&' + encodeURIComponent('ftype') + '=' + encodeURIComponent('ODAM'); /*ODAM: 동네예보실황 -VSRT: 동네예보초단기 -SHRT: 동네예보단기*/
queryParams_Vilage += '&' + encodeURIComponent('base_date') + '=' + encodeURIComponent(full_date); /**/
queryParams_Vilage += '&' + encodeURIComponent('base_time') + '=' + encodeURIComponent(twoHourAgo); /*단기 예보는 아침 8시*/
queryParams_Vilage += '&' + encodeURIComponent('nx') + '=' + encodeURIComponent('55'); /**/
queryParams_Vilage += '&' + encodeURIComponent('ny') + '=' + encodeURIComponent('127'); /**/

fetch(url_Vilage + queryParams_Vilage, {
}).then((response) => response.json())
.then((data) => {
	console.log(data);
}
);


/* 초단기 실황 조회 (지역)*/

var url_UltraSrtNcst = 'http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst'; /*URL*/


var queryParams_UltraSrtNcst = '?' + encodeURIComponent('serviceKey') + '='+apiKey; /*Service Key*/
queryParams_UltraSrtNcst += '&' + encodeURIComponent('pageNo') + '=' + encodeURIComponent('1'); /**/
queryParams_UltraSrtNcst += '&' + encodeURIComponent('numOfRows') + '=' + encodeURIComponent('100'); /**/
queryParams_UltraSrtNcst += '&' + encodeURIComponent('dataType') + '=' + encodeURIComponent('JSON'); /**/
queryParams_UltraSrtNcst += '&' + encodeURIComponent('base_date') + '=' + encodeURIComponent(full_date); /**/
queryParams_UltraSrtNcst += '&' + encodeURIComponent('base_time') + '=' + encodeURIComponent(twoHourAgo); /**/
queryParams_UltraSrtNcst += '&' + encodeURIComponent('nx') + '=' + encodeURIComponent('55'); /**/
queryParams_UltraSrtNcst += '&' + encodeURIComponent('ny') + '=' + encodeURIComponent('127'); /**/
/*
fetch(url_UltraSrtNcst + queryParams_UltraSrtNcst, {
}).then((response) => response.json())
.then((data) => console.log(data));
*/