package com.habibi.loginwithsqlite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    EditText studentId, studentName, studentCourse;
    ImageButton addStudentButton, updateStudentButton, deleteStudentButton;
    ListView studentListView;
    DatabaseHelper db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        db = new DatabaseHelper(this);
        studentId = findViewById(R.id.studentId);
        studentName = findViewById(R.id.studentNames);
        studentCourse = findViewById(R.id.studentCourse);
        addStudentButton = findViewById(R.id.addStudentButton);
        updateStudentButton = findViewById(R.id.updateStudentButton);
        deleteStudentButton = findViewById(R.id.deleteStudentButton);
        studentListView = findViewById(R.id.studentListView);

        loadStudentData();
        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = studentName.getText().toString();
                String course = studentCourse.getText().toString();
                if (db.insertStudent(name, course)) {
                    Toast.makeText(DashboardActivity.this, "Student Added",
                            Toast.LENGTH_SHORT).show();
                    loadStudentData();
                } else {
                    Toast.makeText(DashboardActivity.this, "Error Adding Student", Toast.LENGTH_SHORT).show();
                }
            }
        });
        updateStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = studentId.getText().toString();
                String name = studentName.getText().toString();
                String course = studentCourse.getText().toString();
                if (db.updateStudent(Integer.parseInt(id), name, course)) {
                    Toast.makeText(DashboardActivity.this, "Student Updated", Toast.LENGTH_SHORT).show();
                            loadStudentData();
                } else {
                    Toast.makeText(DashboardActivity.this, "Error Updating Student", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = studentId.getText().toString();
                if (db.deleteStudent(Integer.parseInt(id)) > 0) {
                    Toast.makeText(DashboardActivity.this, "Student Deleted", Toast.LENGTH_SHORT).show();
                            loadStudentData();
                } else {
                    Toast.makeText(DashboardActivity.this, "Error Deleting Student", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void loadStudentData() {
        Cursor cursor = db.getAllStudents();
        String[] from = new String[]{"_id","name", "course"};
        int[] to = new int[]{R.id.studentNameView, R.id.studentCourseView};
        // Adjust the SimpleCursorAdapter
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.student_list_item, cursor, from, to, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.student_list_item, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                // Manually bind your data if needed, especially for views that require specific IDs
                TextView idView = view.findViewById(R.id.studentIdView);
                TextView nameView = view.findViewById(R.id.studentNameView);
                TextView courseView = view.findViewById(R.id.studentCourseView);

                // Fetch data from cursor
                String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String course = cursor.getString(cursor.getColumnIndexOrThrow("course"));

                // Set the data to the views
                idView.setText(id);
                nameView.setText(name);
                courseView.setText(course);
            }
        };
        studentListView.setAdapter(adapter);

        // Set an item click listener to open a new activity with the clicked item data
        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Move the cursor to the clicked position
                cursor.moveToPosition(position);

                // Retrieve data from the cursor
                String studentId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String studentName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String studentCourse = cursor.getString(cursor.getColumnIndexOrThrow("course"));

                // Find EditTexts in the main layout
                EditText idEdit = findViewById(R.id.studentId);
                EditText nameEdit = findViewById(R.id.studentNames);
                EditText courseEdit = findViewById(R.id.studentCourse);

                // Set the data to the EditTexts
                idEdit.setText(studentId);
                nameEdit.setText(studentName);
                courseEdit.setText(studentCourse);
            }
        });
    }
}



