package com.project.sms.Controller;

import com.project.sms.Bean.Familier;
import com.project.sms.Bean.Stu;
import com.project.sms.Bean.Teacher;
import com.project.sms.Bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class SmsController{

    @Autowired
    private StuRepository repository;

    @Autowired
    private TeaRepository teaRepository;

    @Autowired
    private FamRepository famRepository;

    @Autowired
    private UserRepository userRepository;

    //查找所有
    @GetMapping("/findall")
    public List<Stu> list(){
        return repository.findAll();
    }

    //通过学号
    @GetMapping("findbyid")
    public Object findById(@RequestParam("id") Integer id){
        return repository.findById(id);
    }
    //通过班级查询
    @GetMapping("findbyclass")
    public Object findByClazz(@RequestParam("clazz") String clazz){
        return repository.findByClazz(clazz);
    }
    //创建家庭成员信息
    @PostMapping("/addfamilier")
    public Familier add(@RequestParam("id") Integer id,@RequestParam("name") String name,
                        @RequestParam("ffamilier") String ffam,@RequestParam("ftel") Long ftel,
                        @RequestParam("fworkplace") String fworkp,@RequestParam("frelative") String frel,
                        @RequestParam("sfamilier") String sfam,@RequestParam("stel") Long stel,
                        @RequestParam("sworkplace") String sworkp,@RequestParam("srelative") String srel){
        Familier fam=new Familier();
        fam.setId(id);
        fam.setName(name);
        fam.setFfamilier(ffam);
        fam.setFtel(ftel);
        fam.setFworkplace(fworkp);
        fam.setFrelative(frel);
        fam.setSfamilier(sfam);
        fam.setStel(stel);
        fam.setSworkplace(sworkp);
        fam.setSrelative(srel);
        return famRepository.save(fam);
    }
    //查看家庭成员信息
    @GetMapping("/findfamilier")
    public Object find(@RequestParam("name") String name){
        return famRepository.findFamilier(name);
    }

    //辅导员查看自己信息
    @GetMapping("/findme")
    public Object findself(@RequestParam("name") String name){
        return teaRepository.findByName(name);
    }
    //修改学生信息通过id
    @PutMapping("/modify/{id}")
    private Stu modifystudent(@PathVariable("id") Integer id, @RequestParam("name") String name,
                            @RequestParam("tel") Long tel, @RequestParam("QQ") Integer qq,
                            @RequestParam("wechatnum") String wechat){
        Optional stu=repository.findById(id);
        if(stu.isPresent()){
            Stu newstu= (Stu) stu.get();
            newstu.setName(name);
            newstu.setTel(tel);
            newstu.setQQ(qq);
            newstu.setWechatnum(wechat);
            return repository.save(newstu);
        }
        return null;
    }

    //通过名字查询学生
    @GetMapping("findbyname")
    public Object findByName(@RequestParam("name") String name){
        return repository.findByName(name);
    }
    //增加学生
    @PostMapping("/addstudent")
    public Stu add(@RequestParam("id") Integer id, @RequestParam("name") String name, @RequestParam("tel") Long tel,
                   @RequestParam("QQ") Integer qq, @RequestParam("wechatnum") String wechat, @RequestParam("college") String college,
                   @RequestParam("mainpro") String mainpro, @RequestParam("clazz") String clazz, @RequestParam("teacher") String teacher){
        Stu stu =new Stu();
        User user=new User();
        stu.setId(id);
        stu.setName(name);
        stu.setTel(tel);
        stu.setQQ(qq);
        stu.setWechatnum(wechat);
        stu.setCollege(college);
        stu.setMainpro(mainpro);
        stu.setClazz(clazz);
        stu.setTeacher(teacher);
        user.setId(id);
        user.setUsername("stu"+id);
        user.setPassword("123456");
        user.setPerm("student");
        userRepository.save(user);
        return repository.save(stu);
    }
    //修改学生信息
    @PutMapping("/modifystudent/{id}")
    public Stu upadte(@PathVariable("id") Integer id,@RequestParam("college") String college,@RequestParam("mainpro") String mianpro){
        Optional stu=repository.findById(id);
        if(stu.isPresent()){
            Stu newstu= (Stu) stu.get();
            newstu.setCollege(college);
            newstu.setMainpro(mianpro);
            return repository.save(newstu);
        }
        return null;
    }
    //删除学生
    @GetMapping("/deletestudent")
    public String del(@RequestParam("id") Integer id){
        repository.deleteById(id);
        userRepository.deleteById(id);
        famRepository.deleteById(id);
        return "删除成功";
    }
    //配置学生班级(bug:设置相应老师)
    @PutMapping("/setclass/{id}")
    public Stu setclass(@PathVariable("id") Integer id,
                        @RequestParam("clazz") String clazz){
        Optional stu=repository.findById(id);
        if(stu.isPresent()){
            Stu newstu=(Stu) stu.get();
            newstu.setClazz(clazz);
            Teacher t=teaRepository.findByClazz(clazz);
            String tea=t.getName();
            newstu.setTeacher(tea);
            return repository.save(newstu);
        }
        return null;
    }
    //查看辅导员信息
    @GetMapping("/findteacher")
    public List<Teacher> findall(){
        return teaRepository.findAll();
    }
    //增加辅导员
    @PostMapping("/addteacher")
    public Teacher add(@RequestParam("id") Integer id,@RequestParam("name") String name,
                       @RequestParam("tel") Long tel,@RequestParam("qq") Integer qq,
                       @RequestParam("wechatnum") String wechat,@RequestParam("clazz") String clazz){
        Teacher teacher=new Teacher();
        User user=new User();
        teacher.setId(id);
        teacher.setName(name);
        teacher.setTel(tel);
        teacher.setQQ(qq);
        teacher.setWechatnum(wechat);
        teacher.setClazz(clazz);
        user.setId(id);
        user.setUsername("T"+id);
        user.setPassword("123456");
        user.setPerm("teacher");
        userRepository.save(user);
        return teaRepository.save(teacher);
    }
    //修改辅导员信息
    @PutMapping("/modifyteacher/{id}")
    public Teacher modify(@PathVariable("id") Integer id,
                          @RequestParam("tel") Long tel,@RequestParam("qq") Integer qq,
                          @RequestParam("wechatnum") String wechat){
        Optional teacher=teaRepository.findById(id);
        if(teacher.isPresent()) {
            Teacher newtea = (Teacher) teacher.get();
            newtea.setTel(tel);
            newtea.setQQ(qq);
            newtea.setWechatnum(wechat);
            return teaRepository.save(newtea);
        }
        return null;
    }
    //删除辅导员
    @GetMapping("/deleteteacher")
    public String deleteteacher(@RequestParam("id") Integer id){
        teaRepository.deleteById(id);
        userRepository.deleteById(id);
        return "删除成功";
    }
    //配置辅导员班级
    @PutMapping("/updateclass/{id}")
    public  String updateclass(@PathVariable("id") Integer id,@RequestParam("clazz") String clazz,
                            @RequestParam("teacher") String teacher){
            Optional tea=teaRepository.findById(id);
            System.out.println("开始if");
            if(tea.isPresent()) {
                Teacher t = (Teacher) tea.get();
                t.setClazz(clazz);
                teaRepository.save(t);
                //repository.updateTeacher(teacher,clazz);
                return "修改成功";
            }
            return null;
    }
}