package com.zjj.coursestudy.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @Column(name = "teacher_name", nullable = false)
    private String teacherName;

    @Column(name = "teacher_contact", nullable = false)
    private String teacherContact;

    @Column(name = "invitation_code", nullable = false)
    private String invitationCode;

    @Column(name = "is_ending", nullable = false)
    private boolean isEnding;

    @JsonManagedReference
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "course", fetch = FetchType.EAGER)
    private List<EvaluationItem> evaluationItems;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JsonIgnoreProperties("courses")
    @JoinTable(name = "student_course_link"
            , joinColumns = @JoinColumn(name = "course_id")
            , inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Set<User> students;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JsonIgnoreProperties("courses")
    @JoinTable(name = "course_key_word_link"
            , joinColumns = @JoinColumn(name = "course_id")
            , inverseJoinColumns = @JoinColumn(name = "key_word_id"))
    private Set<KeyWord> keyWords;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherContact() {
        return teacherContact;
    }

    public void setTeacherContact(String teacherContact) {
        this.teacherContact = teacherContact;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public boolean isEnding() {
        return isEnding;
    }

    public void setEnding(boolean ending) {
        isEnding = ending;
    }

    public List<EvaluationItem> getEvaluationItems() {
        return evaluationItems;
    }

    public void setEvaluationItems(List<EvaluationItem> evaluationItems) {
        this.evaluationItems = evaluationItems;
    }

    public Set<User> getStudents() {
        return students;
    }

    public void setStudents(Set<User> students) {
        this.students = students;
    }

    public void addStudent(User user){
        this.students.add(user);
    }

    public void removeStudent(User user){
        this.students.remove(user);
    }

    public Set<KeyWord> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(Set<KeyWord> keyWords) {
        this.keyWords = keyWords;
    }
}
