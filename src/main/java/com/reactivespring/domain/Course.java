package com.reactivespring.domain;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "course")
public class Course {

	@Id
	@Column(value = "course_id")
	private Long courseId;

	@NotBlank(message = "course.name must be present")
	private String name;

	private String description;

	@Transient
	private List<CourseReview> reviews;

	public Course() {
	}

	public Course(Long courseId, String name, String description) {
		super();
		this.courseId = courseId;
		this.name = name;
		this.description = description;
	}

	public Course(Long courseId, String name, String description, List<CourseReview> reviews) {
		super();
		this.courseId = courseId;
		this.name = name;
		this.description = description;
		this.reviews = reviews;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<CourseReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<CourseReview> reviews) {
		this.reviews = reviews;
	}

	@Override
	public String toString() {
		return "Course [courseId=" + courseId + ", name=" + name + ", description=" + description + ", reviews="
				+ reviews + "]";
	}

}
