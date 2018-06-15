(function($){
	var contextPath = "/admin/";
	$.extend({
		//格式化日期函数，即毫秒转成日期格式
		formatDate:function(ms,pattern){
			if(!ms){
				throw Error("you must pass in a parameter!");
			}
			var p = "yyyy-MM-dd HH:mm:ss";//默认格式;
			if(pattern){
				p = pattern;
			}
			var date = new Date(ms);
			var year = date.getFullYear();
			/*if(year<1900){
				year = year + 1900;
			}*/
			var month = date.getMonth()+1;
			var d = date.getDate();
			var hour = date.getHours();
			var minute = date.getMinutes();
			var second = date.getSeconds();
			if(month < 10){
				month = "0"+month;
			}
			if(d < 10){
				d = "0"+d;
			}
			if(hour < 10){
				hour = "0"+hour;
			}
			if(minute < 10){
				minute = "0"+minute;
			}
			if(second < 10){
				second = "0"+second;
			}
			var strdate = p.replace("yyyy",year).replace("MM",month).replace("dd",d).replace("HH",hour).replace("mm",minute).replace("ss",second);
			return strdate;
		},
		myAjax:function(options){
			$.ajax({
				url:options.url,
				type:options.type,
				data:options.data,
				dataType:"json",
				success:function(data){
					if(data.code === Code.SESSION_EXPIRED.code){
						window.open(data.data,"_top");
					}else if(data.code === Code.NO_AUTH.code){
						alert(data.message);
					}else{
						options.success(data);
					}
				}
			});
		},
		datagridFormatDate:function(value){
			if(value){
				return $.formatDate(value);
			}
			return value;
		}
	});
	
})(jQuery);
//对Date的扩展，将 Date 转化为指定格式的String
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
//例子： 
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.format = function (fmt) { //author: meizz 
	 var o = {
	     "M+": this.getMonth() + 1, //月份 
	     "d+": this.getDate(), //日 
	     "h+": this.getHours(), //小时 
	     "m+": this.getMinutes(), //分 
	     "s+": this.getSeconds(), //秒 
	     "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
	     "S": this.getMilliseconds() //毫秒 
	 };
	 if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	 for (var k in o)
	 if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	 return fmt;
	 
};
Array.prototype.indexOf = function(val) {
	for (var i = 0,len = this.length; i < len; i++) {
		if (this[i] == val) return i;
	}
	return -1;
};
Array.prototype.remove = function(val) {
	var index = this.indexOf(val);
	if (index > -1) {
		this.splice(index, 1);
	}
};
Array.prototype.indexOfById = function(val) {
	for (var i = 0,len = this.length; i < len; i++) {
		if (this[i].id == val) return i;
	}
	return -1;
};
Array.prototype.removeById = function(val) {
	var index = this.indexOfById(val);
	if (index > -1) {
		this.splice(index, 1);
	}
};