package com.example.crudfirestoredatabase

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import java.util.UUID

class MainActivity: ComponentActivity() {
    val greenColor = Color(0xFF4CAF50)
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CRUDFirestoreDatabaseTheme {
                // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background,
                        ) {


                            Scaffold(
                                // in scaffold we are specifying the top bar.
                                topBar = {
                                    // inside top bar we are specifying
                                    // background color.
                                    TopAppBar(backgroundColor = greenColor,
                                        // along with that we are specifying
                                        // title for our top bar.
                                        title = {
                                            // in the top bar we are
                                            // specifying tile as a text
                                            Text(
                                                // on below line we are specifying
                                                // text to display in top app bar
                                                text = "GFG",
                                                // on below line we are specifying
                                                // modifier to fill max width
                                                modifier = Modifier.fillMaxWidth(),
                                                // on below line we are
                                                // specifying text alignment
                                                textAlign = TextAlign.Center,
                                                // on below line we are specifying
                                                // color for our text.
                                                color = Color.White
                                            )
                                        })
                                }) {innerPadding ->
                                Text(
                                    modifier = Modifier.padding(innerPadding),
                                    text = "Them du lieu."
                                )
                                // on below line we are calling
                                // method to display UI
                                FirebaseUI(LocalContext.current)
                            }
                        }
            }
        }
    }
}





@Composable
fun FirebaseUI(context: Context) {
    val courseName = remember {
        mutableStateOf("")
    }

    val courseDuration = remember {
        mutableStateOf("")
    }

    val courseDescription = remember {
        mutableStateOf("")
    }

    // on below line creating a column
    // to display our retrieved image view.
    Column(
        // adding modifier for our column
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.White),
        // on below line adding vertical and
        // horizontal alignment for column.
        verticalArrangement = Arrangement.Center, horizontalAlignment =
            Alignment.CenterHorizontally
    ) {


        TextField(
            // on below line we are specifying
            // value for our course name text field.
            value = courseName.value,

            // on below line we are adding on
            // value change for text field.
            onValueChange = { courseName.value = it },

            // on below line we are adding place holder
            // as text as "Enter your course name"
            placeholder = { Text(text = "Enter your course name") },

            // on below line we are adding modifier to it
            // and adding padding to it and filling max width
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),

            // on below line we are adding text style
            // specifying color and font size to it.
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

            // on below line we are adding
            // single line to it.
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            // on below line we are specifying
            // value for our course duration text field.
            value = courseDuration.value,

            // on below line we are adding on
            // value change for text field.
            onValueChange = { courseDuration.value = it },

            // on below line we are adding place holder
            // as text as "Enter your course duration"
            placeholder = { Text(text = "Enter your course duration") },

            // on below line we are adding modifier to it
            // and adding padding to it and filling max width
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),

            // on below line we are adding text style
            // specifying color and font size to it.
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

            // on below line we are adding
            // single line to it.
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            // on below line we are specifying
            // value for our course description text field.
            value = courseDescription.value,

            // on below line we are adding on
            // value change for text field.
            onValueChange = { courseDescription.value = it },

            // on below line we are adding place holder
            // as text as "Enter your course description"
            placeholder = { Text(text = "Enter your course description") },

            // on below line we are adding modifier to it
            // and adding padding to it and filling max width
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),

            // on below line we are adding text style
            // specifying color and font size to it.
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

            // on below line we are adding
            // single line to it.
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(10.dp))

        // on below line creating button to add data
        // to firebase firestore database.
        Button(
            onClick = {
                // on below line we are validating user input parameters.
                if (TextUtils.isEmpty(courseName.value)) {
                    Toast.makeText(context, "Please enter course name",
                        Toast.LENGTH_SHORT).show()
                } else if
                               (TextUtils.isEmpty(courseDuration.value)) {
                    Toast.makeText(context, "Please enter course Duration",
                        Toast.LENGTH_SHORT)
                        .show()
                } else if
                               (TextUtils.isEmpty(courseDescription.value)) {
                    Toast.makeText(context, "Please enter course description", Toast.LENGTH_SHORT)
                        .show()
                } else {

                    //  val courseID: UUID = UUID.randomUUID()
                    // on below line adding data to firebase firestore database.
                    addDataToFirebase(
                        courseName.value,
                        courseDuration.value,
                        courseDescription.value,
                        context
                    )


                }
            },
            // on below line we are
            // adding modifier to our button.
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // on below line we are adding text for our button
            Text(text = "Add Data", modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                // on below line opening course details activity.
                context.startActivity(
                    Intent(
                        context,
                        CourseDetailsActivity::class.java
                    )
                )
            },
            // on below line we are
            // adding modifier to our button.
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // on below line we are adding text for our button
            Text(text = "View Courses", modifier = Modifier.padding(8.dp))
        }


    }
}


fun addDataToFirebase(
    courseName: String,
    courseDuration: String,
    courseDescription: String,
    context: Context
) {
    val db = FirebaseFirestore.getInstance()
    val courseID = UUID.randomUUID().toString()

    val course = Course(
        courseID,
        courseName,
        courseDuration,
        courseDescription
    )

    db.collection("Courses")
        .document(courseID)
        .set(course)
        .addOnSuccessListener {
            Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Lỗi: $e", Toast.LENGTH_SHORT).show()
        }
}
