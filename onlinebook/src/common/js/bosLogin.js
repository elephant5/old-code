
	// 事件队列
	var _mevents = new Array();

	function setWebitEvent(evtName, evtCode) {
		if (evtName == "") {
			return;
		}
		_mevents.push(JSON.stringify({
			code : evtCode,
			name : evtName
		}));
	}

/**
	 *   会话保持
	 *   
	 *   keepAlive:会话保持标志
	 */
	function keepAlive() {
		try {
			var jsonParamObj = {
				type : "keepAlive",
				callback : "getKeepAliveCallBack",
				encrypt : "AES"
			}
			var jsonParam = JSON.stringify(jsonParamObj)
			window.SysClientJs.getUserInfo(jsonParam);
		} catch (e) {
			setWebitEvent(jsonParam, "C05")
		}
	}
	/*
	* 用户信息回调
	* */
	function getKeepAliveCallBack(data){
		var json = {};
		json = eval("(" + data + ")");//data为加密数据，需求根据与“encrypt”字段对应的解密方式进行解密方可使用
		// alert("STATUS:" + json.STATUS); // STATUS为1时表示请求成功MSG为请求返回数据,为0时表示请求失败MSG为报错话术,为005时表示登陆超时MSG为报错话术,当为005时调用cleanLoginStatus()方法
		// alert("MSG:" + json.MSG); //  加密内容
		if('005' == json.STATUS){
			cleanLoginStatus()
		}
	}



	/**
	 * 清除登录状态，当isNewUserFun方法中json.STATUS为005时调用，
	 * */
	function cleanLoginStatus() {
		try {
			window.SysClientJs.cleanLoginStatus();
		} catch (e) {
			setWebitEvent("1","14");
		}
		getLogin();
	}
    export { //输出
        keepAlive,
        getKeepAliveCallBack,
        cleanLoginStatus
    } 