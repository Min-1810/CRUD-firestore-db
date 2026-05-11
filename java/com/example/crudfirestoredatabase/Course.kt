package com.example.crudfirestoredatabase

import com.google.firebase.firestore.Exclude

data class Course(
    // Sử dụng @Exclude để Firestore không tự tạo thêm một field "courseID" dư thừa bên trong Document
    @get:Exclude @set:Exclude var courseID: String? = null,
    var courseName: String? = null,
    var courseDuration: String? = null,
    var courseDescription: String? = null
) {
    // Constructor không tham số bắt buộc phải có cho Firestore .toObject()
    constructor() : this(null, null, null, null)
}