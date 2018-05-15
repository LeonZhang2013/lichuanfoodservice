package com.lichuan.sale.web;

import com.alibaba.fastjson.JSON;
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
//	@ApiMethod(summary = "获得地域列表",
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
	 *
	 * @param phone 获取电话号码
	 * @return
	 */
	@RequestMapping("getVerCode")
	public SingleResult<Map<String, Object>> getVerCdoe(String phone,boolean update) {
		SingleResult<Map<String, Object>> result = new SingleResult<>();
		try {
			//如果是更新密码，就不用验证手机号是否存在。
			if(!update)userService.phoneHasEixst(phone);
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
