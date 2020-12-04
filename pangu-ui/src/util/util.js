export function formatDate(date, formatstr) {
  let format = formatstr;
  const o = {
    'M+': date.getMonth() + 1,
    'd+': date.getDate(),
    'h+': date.getHours(),
    'H+': date.getHours(),
    'm+': date.getMinutes(),
    's+': date.getSeconds(),
    'q+': Math.floor((date.getMonth() + 3) / 3),
    S: date.getMilliseconds(),
  };
  if (/(y+)/.test(format)) {
    format = format.replace(RegExp.$1, `${date.getFullYear()}`.substr(4 - RegExp.$1.length));
  }
  for (const k in o) {
    if (new RegExp(`(${k})`).test(format)) {
      format = format.replace(RegExp.$1, RegExp.$1.length === 1 ? o[k] : (`00${o[k]}`).substr(`${o[k]}`.length));
    }
  }
  return format;
};
export function getHttpPro(){
  // console.info(window.location.href)
  const href=window.location.href;
  if(href && href.indexOf('https')>-1){
    return 'https://'
  }
  return 'http://'
}
export function getCodeList(input){
  // console.info("getCodeList input:"+input)
  let result=input;
  if(result){
      //替换所有的换行符为逗号
      result=result.replace(/\r\n/g,",");
      result=result.replace(/\n/g,",");
      result=result.replace(/\r/g,",");
      //替换所有的空格
      result=result.replace(/ /g,"")
      //替换所有的双逗号为逗号
      result=result.replace(/,,/g,",")
  }
  // console.info("getCodeList result:"+result)

  return result;
}