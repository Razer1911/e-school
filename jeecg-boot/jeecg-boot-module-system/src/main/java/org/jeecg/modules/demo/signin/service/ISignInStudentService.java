package org.jeecg.modules.demo.signin.service;

import org.jeecg.modules.demo.signin.entity.SignInStudent;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 学生附表
 * @Author: jeecg-boot
 * @Date:   2020-04-25
 * @Version: V1.0
 */
public interface ISignInStudentService extends IService<SignInStudent> {

	public List<SignInStudent> selectByMainId(String mainId);
}
