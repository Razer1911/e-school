package org.jeecg.modules.demo.studentattendance.service.impl;

import org.jeecg.modules.demo.studentattendance.entity.StudentAttendance;
import org.jeecg.modules.demo.studentattendance.mapper.StudentAttendanceMapper;
import org.jeecg.modules.demo.studentattendance.service.IStudentAttendanceService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 学生出勤统计
 * @Author: jeecg-boot
 * @Date:   2020-04-27
 * @Version: V1.0
 */
@Service
public class StudentAttendanceServiceImpl extends ServiceImpl<StudentAttendanceMapper, StudentAttendance> implements IStudentAttendanceService {

}
