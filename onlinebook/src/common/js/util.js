//转换订单状态
exports.transformatStatus = (status,useStatus,payStatus,serviceType,expressStatus) =>{
    var tips={};
    var statusTxt;
    var statusTip ="";
    //兑换券状态处理
    if(serviceType!=null&&serviceType.indexOf('_cpn')!=-1){
      console.log(expressStatus)
      if(payStatus == 1){
        statusTxt = "未支付";
      }else{
        if(serviceType == 'object_cpn'){
            if(expressStatus == '0'){ //0未发货1已发货2已收货3已退货
              statusTxt = "兑换成功";
            } else if(expressStatus == '1'){
              statusTxt = "已发货";
            } else if(expressStatus == '3'){
              statusTxt = "已退货";
            }
        }else{
          statusTxt = "兑换成功";
        }
        statusTip = '感谢您的信任，期待再次光临！'
      }
    }else {
      if(status==0){
        if(payStatus == 1){
          statusTxt = "未支付";
          statusTip = " "
        }else{
          statusTxt = "待处理";
          if(serviceType == 'medical'){
            statusTip = "5个工作日内，根据持卡人申请和病情完成预约对症专家门诊或专家特需门诊服务";
          }else{
            statusTip = "预计3-5工作日内短信告知您处理结果";
          }
        }
        
        
      }else if(status==1){
        if(useStatus==1||useStatus==0){
          statusTxt = "可使用";
          //请您按时前往酒店使用，逾期将被视作已使用（其他）/感谢您的信任，期待您的再次使用（兑换券）
          statusTip ="请您按时前往酒店使用，逾期将被视作已使用";
        }else{
          statusTxt = "已使用";
          statusTip ="感谢您的信任，期待您的再次使用";
        }
      }else if(status==2){
        statusTxt = "预约取消";
        if(serviceType == 'medical'){
          statusTip = "感谢您的信任";
        }else{
          statusTip = "感谢您的信任，期待您的再次使用";
        }
      }else if(status==3){
        statusTxt = "预约失败";
        statusTip = "很抱歉，您的预约失败，您可再次尝试";
      }else if(status==4){
        statusTxt = "处理中";
        if(serviceType == 'medical'){
          statusTip = "5个工作日内，根据持卡人申请和病情完成预约对症专家门诊或专家特需门诊服务";
        }else{
          statusTip = "预计3-5工作日内短信告知您处理结果";
        }
      }
    }
    tips.statusTxt = statusTxt;
    tips.statusTip = statusTip;
    return tips;
  };
  exports.transformat = (tips) =>{
    return tips.statusTxt;
  };
  //转换权益状态
  exports.getGiftTxt = (giftType) =>{
        var giftTxt="";
        switch (giftType) {
          case "2F1":
            // giftTxt = "二免一";
            giftTxt = "两人同行享五折优惠";
            break;
          case "3F1":
            // giftTxt = "三免一";
            giftTxt = "三人同行享六折优惠";
            break;
          case "B1F1":
            giftTxt = "买一赠一";
            break;
          case "D5":
            giftTxt = "五折尊享";
            break;
          case "F1":
            giftTxt = "单人礼遇";
            break;
          case "F2":
            giftTxt = "双人礼遇";
            break;
          case "N1":
            giftTxt = "两天一晚";
            break;
          case "N2":
            giftTxt = "三天两晚";
            break;
          case "N3":
            giftTxt = "四天三晚";
            break;
          case "N4":
            giftTxt = "五天四晚";
            break;
          case "NX":
            giftTxt = "开放住宿";
            break;
        }
       return giftTxt;
  };
  exports.setTipText=(tip) =>{
    var tipText = "";
    switch (tip) {
      case "1":
        tipText = "订单确认后不可取消或变更";
        break;
      case "2":
        tipText = "订单确认后可提前24小时取消或变更";
        break;
      case "3":
        tipText = "订单确认后可提前48小时取消或变更";
        break;
    }
    return tipText;
  };
  //到店付价格  预约时间  产品组产品id 类型   总人数
  exports.getStoreNum=(giftType,num) =>{
      let flag = /\d{0,1}F\d$/.test(giftType);
      if(flag){
        let n =Number(giftType.replace(/\d{0,1}F/,""));
        return num>n?num-n:0;
      }else{
        return num;
      }
  };
  //格式化日期
  exports.dateFormat = function (date) {
    let array = date.match(/\d{4}-\d{2}-\d{2}/)[0].split('-');
    return array[0]+"年"+array[1]+"月"+array[2]+"日";
  }
  juageTime=(date,time)=>{
      var currentTime = new Date();
      if(/^\d{2}-\d{2}$/.test(date)){
          date = currentTime.getFullYear()+"-"+date;
      }
      var dateStr = !!time?date+" 23:59:59"+time:date;
      var d = new Date(dateStr);
      return currentTime.getTime()>d.getTime();
  };