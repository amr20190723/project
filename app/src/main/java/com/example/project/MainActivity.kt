package com.example.project
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import com.example.mediaplayerservice.NewService
import android.view.MenuInflater as MenuInflater1


class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var flag: String ="American"
        val SpinnerVal : Spinner=findViewById(R.id.spinner);
        var options = arrayOf("American","italian","arabic")
        val BTN : Button = view.findViewById(R.id.button2);
        BTN.setOnClickListener(){
            if (flag ="American")
            {

            }
        }
        SpinnerVal.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options )
        SpinnerVal.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                flag = "italian";
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


            val toggle=findViewById<View>(R.id.toggleButton) as ToggleButton
        toggle.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                startService(Intent(this, NewService::class.java))
            } else {
                stopService(Intent(this, NewService::class.java))
            }
}

        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuinflator
        inflater.inflate(R.menu.main_menue, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item1 -> {
                val mapIntent: Intent = Uri.parse(
                    "geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California"
                ).let { location ->
                    Intent(Intent.ACTION_VIEW, location)
                }
                startActivity(mapIntent);}
            R.id.item2 -> {
                var dialog_var = CustomDialogFragment()
                dialog_var.show(supportFragmentManager, "Custom Dialog")
            }

        }
        return true;
    }

