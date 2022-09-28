package com.reactivespring.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "course_review")
public class CourseReview {

	@Id
	@Column(value = "review_id")
	private Long reviewId;

	private String comments;

	private ReviewRating rating;

	@Column(value = "course_id")
	private Long courseId;

	public CourseReview() {
	}

	public CourseReview(Long reviewId, String comments, ReviewRating rating, Long courseId) {
		super();
		this.reviewId = reviewId;
		this.comments = comments;
		this.rating = rating;
		this.courseId = courseId;
	}

	public Long getReviewId() {
		return reviewId;
	}

	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public ReviewRating getRating() {
		return rating;
	}

	public void setRating(ReviewRating rating) {
		this.rating = rating;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	@Override
	public String toString() {
		return "CourseReview [reviewId=" + reviewId + ", comments=" + comments + ", rating=" + rating + ", courseId="
				+ courseId + "]";
	}

}
