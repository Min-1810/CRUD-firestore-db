package com.example.crudfirestoredatabase

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crudfirestoredatabase.ui.theme.CRUDFirestoreDatabaseTheme
import com.google.firebase.firestore.FirebaseFirestore

class UpdateCourse : ComponentActivity() {

    private val greenColor = Color(0xFF0F9D58)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CRUDFirestoreDatabaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                backgroundColor = greenColor,
                                title = {
                                    Text(
                                        text = "Update Course",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                }
                            )
                        }
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                        ) {
                            // Gọi UI nhập liệu cập nhật
                            FirebaseUI(
                                context = LocalContext.current,
                                name = intent.getStringExtra("courseName"),
                                duration = intent.getStringExtra("courseDuration"),
                                description = intent.getStringExtra("courseDescription"),
                                courseID = intent.getStringExtra("courseID")
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun FirebaseUI(
        context: Context,
        name: String?,
        duration: String?,
        description: String?,
        courseID: String?
    ) {
        val courseName = remember { mutableStateOf(name ?: "") }
        val courseDuration = remember { mutableStateOf(duration ?: "") }
        val courseDescription = remember { mutableStateOf(description ?: "") }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Trường nhập tên khóa học
            TextField(
                value = courseName.value,
                onValueChange = { courseName.value = it },
                placeholder = { Text(text = "Enter your course name") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Trường nhập thời gian học
            TextField(
                value = courseDuration.value,
                onValueChange = { courseDuration.value = it },
                placeholder = { Text(text = "Enter your course duration") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Trường nhập mô tả khóa học
            TextField(
                value = courseDescription.value,
                onValueChange = { courseDescription.value = it },
                placeholder = { Text(text = "Enter your course description") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Nút bấm cập nhật dữ liệu
            Button(
                onClick = {
                    if (TextUtils.isEmpty(courseName.value.trim())) {
                        Toast.makeText(context, "Please enter course name", Toast.LENGTH_SHORT).show()
                    } else if (TextUtils.isEmpty(courseDuration.value.trim())) {
                        Toast.makeText(context, "Please enter course duration", Toast.LENGTH_SHORT).show()
                    } else if (TextUtils.isEmpty(courseDescription.value.trim())) {
                        Toast.makeText(context, "Please enter course description", Toast.LENGTH_SHORT).show()
                    } else {
                        updateDataToFirebase(
                            courseID,
                            courseName.value,
                            courseDuration.value,
                            courseDescription.value,
                            context
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = greenColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Update Data",
                    modifier = Modifier.padding(8.dp),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    private fun updateDataToFirebase(
        courseID: String?,
        name: String?,
        duration: String?,
        description: String?,
        context: Context
    ) {
        if (courseID.isNullOrEmpty()) {
            Toast.makeText(context, "Không tìm thấy ID để cập nhật!", Toast.LENGTH_SHORT).show()
            return
        }

        val db = FirebaseFirestore.getInstance()

        // Tạo map dữ liệu cập nhật
        val updatedData = mapOf(
            "courseName" to name,
            "courseDuration" to duration,
            "courseDescription" to description
        )

        // Tiến hành cập nhật chính xác tài liệu thông qua ID
        db.collection("Courses").document(courseID)
            .update(updatedData)
            .addOnSuccessListener {
                Toast.makeText(context, "Course Updated successfully..", Toast.LENGTH_SHORT).show()

                // Quay lại màn hình danh sách và làm mới stack
                val i = Intent(context, CourseDetailsActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(i)

                // Đóng màn hình cập nhật hiện tại
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Fail to update course: " + e.message, Toast.LENGTH_SHORT).show()
                Log.e("FIRESTORE_ERROR", "Cập nhật thất bại", e)
            }
    }
}