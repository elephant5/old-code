
var ENV = "dev"; // 控制开关,dev 开发环境，release,master环境时改为prod
export default {
  pathUrl:function() {
    var URLS = {
      dev: {
        MJK: '/api',
        WEB_URL: {
          WEB_MIAO: 'https://app.colourfulchina.com'
        }
      },
      prod: {
        MJK: 'https://app.colourfulchina.com',
        WEB_URL: {
          WEB_MIAO: 'https://app.colourfulchina.com'
        }
      }
    }
    return URLS[ENV];
  }
}
