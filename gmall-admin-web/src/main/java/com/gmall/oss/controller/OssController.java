package com.gmall.oss.controller;
import com.gmall.oss.component.OssCompent;
import com.gmall.to.CommonResult;
import com.gmall.to.OssPolicyResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Oss相关操作接口
 */
//@CrossOrigin(origins = "www.baidu.com")限制跨域的服务器，从baidu服务器发过来的请求允许跨域
@CrossOrigin
@Controller
@Api(tags = "OssController",description = "Oss管理，获取签名")
@RequestMapping("/aliyun/oss")
public class OssController {
	@Autowired
	private OssCompent ossComponent;

	@ApiOperation(value = "oss上传签名生成")
	@GetMapping(value = "/policy")
	@ResponseBody
	public Object policy() {
		OssPolicyResult result = ossComponent.policy();
		return new CommonResult().success(result);
	}
}
