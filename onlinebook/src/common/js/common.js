
function getChannge(name){
    return decodeURIComponent(
        (new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null;
    }
function gotoWxPay(data, callback) {
        wx.chooseWXPay({
        timestamp: data.timestamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
        nonceStr: data.nonceStr, // 支付签名随机串，不长于 32 位
        package: data.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
        signType: data.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
        paySign: data.paySign, // 支付签名
        success: function (res) {
        // alert('paysuccess -- success')
        // // 支付成功后的回调函数
        // alert(res);
        callback('paysuccess');
        },
        fail: function (res) {
        callback('payfail')
        },
        cancel: function (res) {
        callback('paycancel')
        }
        })
    }

function WXbodyBottomshow(){
        let ua = window.navigator.userAgent.toLowerCase()
        if (ua.match(/MicroMessenger/i)=="micromessenger") {
            // 微信内
            // document.write("<script language='javascript' src='https://res.wx.qq.com/open/js/jweixin-1.3.2.js'></script>");
            // wx.miniProgram.getEnv((res)=>{
            //     if (res.miniprogram) {
            //         //小程序内
            //     } else {
            //         //小程序外
            //         document.getElementById('app').style.paddingBottom='.88rem'
            //     }
            //  })
            return true
        }else if(ua.match(/AlipayClient/i) == 'alipayclient'){
            //支付宝内
            // document.writeln('<script src="https://appx/web-view.min.js"' + '>' + '<' + '/' + 'script>');

            return true
        }else if(ua.indexOf('shengbei') > -1 || ua.indexOf('dtgber+8hplnx4sbflj+lpd1smm=') > -1 || localStorage.getItem('CHANNEL') == 'CMB'){
            //第三方APP内
            return true
        }else{
            // 外部
            return false
        }
    }

    // offset 日期偏移量   获取前一天 -1  后一天 1
    function getNewdate(offset){
        var date = new Date();
        if(!!offset){
           date=date.setDate(date.getDate()+offset);
           date =new Date(date);
        }
        
        var tmpM = "00" + (date.getMonth() + 1);
        var tmpD = "00" + date.getDate();
        var dStr = date.getFullYear() +"," + tmpM.slice(tmpM.length - 2, tmpM.length) + "," + tmpD.slice(tmpD.length - 2, tmpD.length);
        return dStr
    }

    let mo = function(e){e.preventDefault();};
    function stopsrc(){
        // document.body.style.overflow='hidden'; 
        // document.body.style.position='fixed';
        // document.body.style.width='100%';
        // document.addEventListener("touchmove",mo,false);//禁止页面滑动
    }

    function movesrc(){
        // document.body.style.overflow='';//出现滚动条
        // document.body.style.position='initial';
        // document.body.style.height='auto';
        // document.removeEventListener("touchmove",mo,false); 
    }


    
    function device() {
        var device = {};
        var ua = navigator.userAgent;
    
        var android = ua.match(/(Android);?[\s\/]+([\d.]+)?/);
        var ipad = ua.match(/(iPad).*OS\s([\d_]+)/);
        var ipod = ua.match(/(iPod)(.*OS\s([\d_]+))?/);
        var iphone = !ipad && ua.match(/(iPhone\sOS)\s([\d_]+)/);
        
        device.ios = device.android = device.iphone = device.ipad = device.androidChrome = device.type = false;
        
        // Android
        if (android) {
        device.os = 'android';
        device.osVersion = android[2];
        device.android = true;
        device.androidChrome = ua.toLowerCase().indexOf('chrome') >= 0;
        }
        if (ipad || iphone || ipod) {
        device.os = 'ios';
        device.ios = true;
        }
        // iOS
        if (iphone && !ipod) {
        device.osVersion = iphone[2].replace(/_/g, '.');
        device.iphone = true;
        }
        if (ipad) {
        device.osVersion = ipad[2].replace(/_/g, '.');
        device.ipad = true;
        }
        if (ipod) {
        device.osVersion = ipod[3] ? ipod[3].replace(/_/g, '.') : null;
        device.iphone = true;
        }
        // iOS 8+ changed UA
        if (device.ios && device.osVersion && ua.indexOf('Version/') >= 0) {
        if (device.osVersion.split('.')[0] === '10') {
            device.osVersion = ua.toLowerCase().split('version/')[1].split(' ')[0];
        }
        }
        
        if(ua.toLowerCase().indexOf('micromessenger') !== -1) {
        device.type = 'wechat';
        }
        
        // Webview
        device.webView = (iphone || ipad || ipod) && ua.match(/.*AppleWebKit(?!.*Safari)/i);
        // Pixel Ratio
        device.pixelRatio = window.devicePixelRatio || 1;
        return device;
  }
  function cleanStorage(){
    // 清除缓存
    localStorage.removeItem('startDate');
    localStorage.removeItem('endDate');
    localStorage.removeItem('personday');
    localStorage.removeItem('maxBookDays');
    localStorage.removeItem('minBookDays');
    localStorage.removeItem('giftnum');
    localStorage.removeItem('personday');
  }

  function formateDate(date, format){
    if (!date) return;
            if (!format) format = "yyyy/MM/dd HH:mm:ss";
            switch (typeof date) {
                case "string":
                    date = new Date(date.replace(/-/g, "/"));
                    break;
                case "number":
                    date = new Date(date);
                    break;
            }
            if (!date instanceof Date) return;
            var dict = {
                "yyyy": date.getFullYear(),
                "M": date.getMonth() + 1,
                "d": date.getDate(),
                "H": date.getHours(),
                "m": date.getMinutes(),
                "s": date.getSeconds(),
                "MM": ("" + (date.getMonth() + 101)).substr(1),
                "dd": ("" + (date.getDate() + 100)).substr(1),
                "HH": ("" + (date.getHours() + 100)).substr(1),
                "mm": ("" + (date.getMinutes() + 100)).substr(1),
                "ss": ("" + (date.getSeconds() + 100)).substr(1)
            };
            return format.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g, function () {
                return dict[arguments[0]];
            });

  }

export { //输出
    getChannge,
    gotoWxPay,
    WXbodyBottomshow,
    stopsrc,
    movesrc,
    device,
    cleanStorage,
    formateDate,
    getNewdate
}      