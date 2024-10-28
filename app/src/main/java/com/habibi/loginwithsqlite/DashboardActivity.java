package com.habibi.loginwithsqlite;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    EditText studentName, studentCourse;
    Button addStudentButton, updateStudentButton, deleteStudentButton;
    ListView studentListView;
    DatabaseHelper db;
    int selectedStudentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        db = new DatabaseHelper(this);

        studentName = findViewById(R.id.studentName);
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
                String name = studentName.getText().toString();
                String course = studentCourse.getText().toString();
                if (selectedStudentId != -1 &&
                        db.updateStudent(selectedStudentId, name, course)) {
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
                if (selectedStudentId != -1 &&
                        db.deleteStudent(selectedStudentId) > 0) {
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
                TextView nameView = view.findViewById(R.id.studentNameView);
                TextView courseView = view.findViewById(R.id.studentCourseView);

                // Fetch data from cursor
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String course = cursor.getString(cursor.getColumnIndexOrThrow("course"));

                // Set the data to the views
                nameView.setText(name);
                courseView.setText(course);
            }
        };
        studentListView.setAdapter(adapter);
    } }
