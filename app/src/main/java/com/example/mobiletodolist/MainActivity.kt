package com.example.mobiletodolist


import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobiletodolist.components.TaskForm
import com.example.mobiletodolist.databinding.ActivityMainBinding
import com.example.mobiletodolist.utils.TaskItemsAdapter
import com.example.mobiletodolist.utils.TaskItemsClickListener
import com.example.mobiletodolist.utils.TaskViewModel


class MainActivity : AppCompatActivity(), TaskItemsClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel

    private val STORAGE_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        taskViewModel.loadData(this)

        binding.addButton.setOnClickListener {
            TaskForm(null).show(supportFragmentManager, "newTaskTag")
        }

        binding.downloadButton.setOnClickListener {
            taskViewModel.downloadFile()
        }

        /*binding.uploadButton.setOnClickListener {
            getDownloadsFolder()
        }*/

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setRecyclerView()

        checkPermission()
    }

    private fun setRecyclerView() {
        val mainActivity = this
        taskViewModel.taskItemsList.observe(this) {
            binding.toDoListRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TaskItemsAdapter(it, mainActivity)
            }
        }
    }

    /*override fun editTaskItem(taskItem: TaskItem) {
        TaskForm(taskItem).show(supportFragmentManager, "newTaskTag")
    }*/

    /*override fun changeCheckedTaskItem(taskItem: TaskItem) {
        taskViewModel.changeChecked(taskItem)
    }*/

    override fun deleteTaskItem(taskItem: TaskItem) {
        taskViewModel.deleteTaskItem(taskItem.Id)
    }


    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE)
        } else {
            null
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"Доступ разрешен",Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this,"Доступ запрещен",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getDownloadsFolder() {
        val intent = Intent().setType("*/*").setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select a file"), 777)
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 777) {
            val selectedFile = data?.data?.path.toString()

            val lastIndex = selectedFile.lastIndexOf('/')

            val child = selectedFile.substring(lastIndex + 1, selectedFile.length)

            if (lastIndex != -1) {
                val file = File("/storage/emulated/0/Download/", "${child}")

                if (file != null) {
                    taskViewModel.changeTaskItemsList(file)

                    Toast.makeText(applicationContext, "Список дел добавлен!", LENGTH_SHORT).show()
                }
            }
        }
    }*/
}