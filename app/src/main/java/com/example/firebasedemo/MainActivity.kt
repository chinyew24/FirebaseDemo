package com.example.firebasedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("Student")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAdd : Button = findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener(){

            val stdID:String = findViewById<TextView>(R.id.tfID).text.toString()
            val stdName:String = findViewById<TextView>(R.id.tfName).text.toString()
            val stdProgramme:String = findViewById<TextView>(R.id.tfProgramme).text.toString()

            myRef.child(stdID).child("Name").setValue(stdName)
            myRef.child(stdID).child("Programme").setValue(stdProgramme)
        }

        //Abstract
        val getData = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var sb = StringBuilder()

                //snapshot referring to Student table
                for(std in snapshot.children){
                    var name = std.child("Name").getValue()
                    sb.append("${name} \n")
                    //var programme = std.child("Programme").getValue()
                }

                val tvResult : TextView = findViewById(R.id.tvResult)
                tvResult.setText(sb)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        val btnGet : Button = findViewById(R.id.btnGet)
        btnGet.setOnClickListener(){

            //display only the students from selected programme
            val qry: Query = myRef.orderByChild("Programme").equalTo("RSD")
            qry.addValueEventListener(getData)
            qry.addListenerForSingleValueEvent(getData)

            //whenever change in the database, fire the ValueEventListener
            //Get data
            //myRef.addValueEventListener(getData)
            //myRef.addListenerForSingleValueEvent(getData)
        }
    }
}