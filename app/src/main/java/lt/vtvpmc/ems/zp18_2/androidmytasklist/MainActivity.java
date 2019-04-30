package lt.vtvpmc.ems.zp18_2.androidmytasklist;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Database db;
    private ListView taskList;
    private ArrayAdapter<String> taskListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = (Database) new Database(this);
        taskList = (ListView) findViewById(R.id.taskList);

        updateTaskList();

    }

    private void updateTaskList() {
        ArrayList<String> tasks = db.getAlltasks();

        if (tasks != null) {
            taskListAdapter = new ArrayAdapter<String>(this, R.layout.row, R.id.TaskText, tasks);
            taskList.setAdapter(taskListAdapter);
        } else {
            taskListAdapter.clear();
            taskListAdapter.addAll(tasks);
            taskListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(R.color.white, null), PorterDuff.Mode.ADD);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.taskAdd) {
            final EditText userTaskText = new EditText(this);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Add new task")
                    .setMessage("Enter task name")
                    .setView(userTaskText)
                    .setPositiveButton("Add task", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String task = String.valueOf(userTaskText.getText());
                            db.insertData(task);
                            updateTaskList();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void taskDelete(View view) {
        View parent = (View) view.getParent();
        TextView taskText = (TextView) parent.findViewById(R.id.TaskText);
        String task = String.valueOf(taskText.getText());
        db.deleteData(task);
        updateTaskList();
    }
}
