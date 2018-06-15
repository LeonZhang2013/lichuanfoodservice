var Code = {
		SUCCESS:{status:1,message:"请求成功"},
		NO_AUTH:{status:4,message:"权限不足"},
		SESSION_EXPIRED:{status:9,message:"登录失效"}
	};
var BASE_PATH = "/admin/";
var USER = BASE_PATH + "user/";
var AUTH = BASE_PATH + "auth/";
var ITEM = BASE_PATH + "item/";
var UNIT = BASE_PATH + "unit/";
var ORDER = BASE_PATH + "order/";
var AREA = BASE_PATH + "area/";
var VERSION = BASE_PATH + "version/";
var CATEGORY = BASE_PATH + "category/";
var ROLE = BASE_PATH + "role/";
var COMMON = BASE_PATH + "common/";
var Url = {
	User:{
		login : USER + "index",
		getUserPage : USER + "getUserPage",
		enableUser : USER + "enableUser",
		enableOper : USER + "enableOper",
		addUser : USER + "addUser",
		getOperPage : USER + "getOperPage",
        getShopPage : USER + "getShopPage",
		addOper : USER + "addOper",
        addShop : USER + "addShop",
		getOperRoleList : USER + "getOperRoleList",
		setRole : USER + "setRole",
		updatePwd : USER + "updatePwd",
		getOperById : USER + "getOperById",
		setAuth  :USER + "setAuth"
	},
	Auth :{
		getUserMenu : AUTH + "getUserMenu"
	},
	Item :{
		getItemPage : ITEM + "getItemPage",
		listItem : ITEM + "listItem",
		saveItem : ITEM + "saveItem",
		getItemById : ITEM + "getItemById",
		updateItem : ITEM + "updateItem",
		updateItemPrice : ITEM + "updateItemPrice"
	},
	Unit:{
		getUnitList : UNIT + "getUnitList"
	},
	Order:{
		getNewOrder : ORDER + "getNewOrder",
		getNewOrderList : ORDER + "getNewOrderList",
		getOrderDetail : ORDER + "getOrderDetail",
		addComment : ORDER + "addComment",
		confirmOrder : ORDER + "confirmOrder",
		exportExcel : ORDER + "exportExcel",
		batchExportExcel : ORDER + "batchExportExcel",
		getOrderList : ORDER + "getOrderList",
		completeOrder : ORDER + "completeOrder",
		confirmPay : ORDER + "confirmPay",
		confirmCheck : ORDER + "confirmCheck",
		confirmSend:ORDER + "confirmSend",
		confirmComplete:ORDER+"confirmComplete",
		confirmCancel:ORDER+"confirmCancel",
		setComment : ORDER + "setComment",
		getOrderDetailByItemId : ORDER + "getOrderDetailByItemId",
		checkOrder : ORDER + "checkOrder",
		payAll:ORDER + "payAll",
		exportCheckOrder : ORDER + "exportCheckOrder",
		hasPurchaseNumExists : ORDER + "hasPurchaseNumExists",
		getOrderOperateLog:ORDER+"getOrderOperateLog"
	},
	Area :{
		getAreaByParentId : AREA + "getAreaByParentId"
	},
	Version : {
		getVersionPage : VERSION + "getVersionPage",
		saveVersion : VERSION + "saveVersion"
	},
	Category : {
		getCategoryList : CATEGORY + "getCategoryList",
		saveCategory : CATEGORY + "saveCategory",
		getCategoryById : CATEGORY + "getCategoryById",
		updateCategory : CATEGORY + "updateCategory"
	},
	Role : {
		getRoleList : ROLE + "getRoleList",
		saveRole : ROLE + "saveRole",
		getRoleById : ROLE + "getRoleById",
		updateRole : ROLE + "updateRole"
	},
	Common:{
		getShopOperList : COMMON + "getShopOperList"
	}
};