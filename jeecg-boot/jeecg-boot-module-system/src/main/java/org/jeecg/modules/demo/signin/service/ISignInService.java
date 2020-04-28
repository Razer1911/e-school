package org.jeecg.modules.demo.signin.service;

import org.jeecg.modules.demo.signin.entity.SignInStudent;
import org.jeecg.modules.demo.signin.entity.SignIn;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 签到表
 * @Author: jeecg-boot
 * @Date:   2020-04-25
 * @Version: V1.0
 */
public interface ISignInService extends IService<SignIn> {

	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);


}
