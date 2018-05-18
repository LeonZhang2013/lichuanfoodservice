package com.lichuan.sale.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lichuan.sale.model.Role;
import com.lichuan.sale.model.Version;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MultiResult;
import com.lichuan.sale.result.SingleResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("common")
public class CommonController extends BaseController {


	@RequestMapping(value="getArea",method={RequestMethod.POST})
	public MultiResult<Map<String,Object>> getArea(String parentId){
		MultiResult<Map<String,Object>> result = new MultiResult<>();
		try {
			if(null != parentId){
				List<Map<String, Object>> area = commonService.getArea(Long.parseLong(parentId));
				result.setCode(Code.SUCCESS);
				result.setData(area);
			}else{
				result.setCode(Code.EXP_PARAM);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Code.ERROR);
		}
		return result;
	}

	/**
	 * 发送验证短信。
	 * @param phone   获取电话号码
	 * @param isRegister 是否是注册 注册 true 否 false
	 * @return
	 */
	@RequestMapping("getVerCode")
	public SingleResult<Map<String, Object>> getVerCdoe(String phone,boolean isRegister) {
		SingleResult<Map<String, Object>> result = new SingleResult<>();
		try {
			//如果是更新密码，就不用验证手机号是否存在。
			if(isRegister)userService.phoneHasEixst(phone);
			result = aliYunService.sendMassage(phone);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Code.ERROR);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * 获取权限。
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getRoles")
	public SingleResult<Object> getRoles(){
		SingleResult<Object> result = new SingleResult<>();
		try {
			List<Role> roles = commonService.getRoles(getUser().getRole_id());
			result.setCode(Code.SUCCESS);
			result.setData(roles);
		} catch (Exception e) {
			result.setCode(Code.ERROR);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * 获取权限。
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getMyPermissions")
	public SingleResult<Object> getPermission(String role_id) {
		SingleResult<Object> result = new SingleResult<>();
		try {
			List<Map<String, Object>> permissions = commonService.getPermissions();
			List<Map<String, Object>> myPermissions = commonService.getMyPermissions(role_id);
			for (int i = 0; i < permissions.size(); i++) {
				Object item = permissions.get(i).get("id");
				permissions.get(i).put("check", "false");
				for (int j = 0; j < myPermissions.size(); j++) {
					Object exit = myPermissions.get(j).get("permission_id");
					if (item.toString().equals(exit.toString())) {
						permissions.get(i).put("check", "true");
					}
				}
			}

			result.setCode(Code.SUCCESS);
			result.setData(permissions);
		} catch (Exception e) {
			result.setCode(Code.ERROR);
			result.setMessage(e.getMessage());
		}
		return result;
	}


	/**
	 *
	 * @param role_id
	 * @param permissions  json列表 {1,2,3}
	 * @return
	 */
	@RequestMapping("updatePermissions")
	public SingleResult<Object> updatePermissions(String role_id, String permissions) {
		SingleResult<Object> result = new SingleResult<>();
		try {
			JSONArray groupJson = JSON.parseArray(permissions);
			commonService.updatePermissions(getUserId(),role_id, groupJson);
			result.setCode(Code.SUCCESS);
			result.setMessage("更新成功");
		} catch (Exception e) {
			result.setCode(Code.ERROR);
			result.setMessage(e.getMessage());
		}
		return result;
	}


	/**
	 * 检查更新
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("checkVersion")
	public SingleResult<Object> checkVersion(Integer code,String packageName){
		SingleResult<Object> result = new SingleResult<>();
		try {
			Version version = commonService.checkVersion(code,packageName);
			result.setCode(Code.SUCCESS);
			if(version!=null) {
				result.setData(JSON.toJSON(version));
			}else {
				result.setMessage("没有数据");
			}
		} catch (Exception e) {
			result.setCode(Code.ERROR);
			result.setMessage(e.getMessage());
		}
		return result;
	}

}
