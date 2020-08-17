package com.xiaoshu.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.dao.CourseMapper;
import com.xiaoshu.dao.StudentMapper;
import com.xiaoshu.entity.Course;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentVo;

import redis.clients.jedis.Jedis;

@Service
public class StudentService {

	@Autowired
	private StudentMapper studentMapper;
	
	@Autowired
	private CourseMapper courseMapper;
	
	public PageInfo<StudentVo> findPage(StudentVo studentVo,Integer pageNum,Integer pageSize){
		PageHelper.startPage(pageNum, pageSize);
		List<StudentVo> list = studentMapper.findAll(studentVo);
		return new PageInfo<>(list);
		
	}
	public void addS(Student student){
		studentMapper.insert(student);
		
	}
	public void updateS(Student student){
		studentMapper.updateByPrimaryKeySelective(student);
	}
	public void addC(Course course){
		course.setCreatetime(new Date());
		courseMapper.insert(course);
		Course param = new Course();
		param.setName(course.getName());
		Course course2 = courseMapper.selectOne(param);
		Jedis jedis = new Jedis("127.0.0.1",6379);
		
		
		jedis.hset("课程", course2.getId()+"",JSONObject.toJSONString(course2));
		
	}
	public List<Course> findAllC(){
		return courseMapper.selectAll();
		
	}
	public List<StudentVo> findE(){
		return studentMapper.findE();
	}


}
