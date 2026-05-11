package com.example.crudfirestoredatabase

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crudfirestoredatabase.ui.theme.CRUDFirestoreDatabaseTheme
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class CourseDetailsActivity : ComponentActivity() {

    // Khai báo màu xanh lá làm biến thành viên (Không truyền vào Constructor của Activity)
    private val greenColor = Color(0xFF0F9D58)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CRUDFirestoreDatabaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "GFG",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = Color.Black
                                    )
                                }
                            )
                        }
                    ) { innerPadding ->
                        // Gom toàn bộ vào trong Column để tránh chồng chéo UI
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                        ) {
                            Text(
                                text = "Danh sách khóa học",
                                modifier = Modifier.padding(16.dp),
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            )

                            // Xác định rõ kiểu dữ liệu <Course> để tránh lỗi ép kiểu (infer type)
                            val courseList = remember { mutableStateListOf<Course>() }
                            val context = LocalContext.current

                            // Gọi Firebase lấy dữ liệu bằng LaunchedEffect (chỉ chạy 1 lần khi mở màn hình)
                            LaunchedEffect(Unit) {
                                val db = FirebaseFirestore.getInstance()
                                db.collection("Courses").get()
                                    .addOnSuccessListener { queryDocumentSnapshots ->
                                        if (!queryDocumentSnapshots.isEmpty) {
                                            // Ép kiểu tường minh cho danh sách document nhận về từ Firestore
                                            val list: List<DocumentSnapshot> = queryDocumentSnapshots.documents

                                            courseList.clear() // Xóa dữ liệu cũ tránh bị lặp
                                            for (d in list) {
                                                val c: Course? = d.toObject(Course::class.java)
                                                if (c != null) {
                                                    c.courseID = d.id // Gán ID tài liệu vào đối tượng Course
                                                    Log.e("TAG", "Course id is : " + c.courseID)
                                                    courseList.add(c) // Thêm vào list state để vẽ lại màn hình
                                                }
                                            }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "No data found in Database",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            context,
                                            "Fail to get the data.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }

                            // Gọi hàm hiển thị giao diện danh sách khóa học
                            FirebaseUI(context, courseList)
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun FirebaseUI(context: Context, courseList: SnapshotStateList<Course>) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.White),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn {
                items(courseList) { item ->
                    Card(
                        onClick = {
                            // Chuyển sang màn hình cập nhật khóa học và truyền dữ liệu
                            val i = Intent(context, UpdateCourse::class.java).apply {
                                putExtra("courseID", item.courseID)
                                putExtra("courseName", item.courseName)
                                putExtra("courseDuration", item.courseDuration)
                                putExtra("courseDescription", item.courseDescription)
                            }
                            context.startActivity(i)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = 6.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            item.courseName?.let {
                                Text(
                                    text = it,
                                    modifier = Modifier.padding(4.dp),
                                    color = greenColor,
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))

                            item.courseDuration?.let {
                                Text(
                                    text = "Duration: $it",
                                    modifier = Modifier.padding(4.dp),
                                    color = Color.Black,
                                    style = TextStyle(fontSize = 15.sp)
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))

                            item.courseDescription?.let {
                                Text(
                                    text = it,
                                    modifier = Modifier.padding(4.dp),
                                    color = Color.DarkGray,
                                    style = TextStyle(fontSize = 15.sp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}