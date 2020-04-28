package org.jeecg.modules.demo.signin.mapper;

import java.util.List;
import org.jeecg.modules.demo.signin.entity.SignInStudent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 学生附表
 * @Author: jeecg-boot
 * @Date:   2020-04-25
 * @Version: V1.0
 */
public interface SignInStudentMapper extends BaseMapper<SignInStudent> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<SignInStudent> selectByMainId(@Param("mainId") String mainId);

}
