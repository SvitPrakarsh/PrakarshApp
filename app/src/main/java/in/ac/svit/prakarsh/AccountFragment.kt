package `in`.ac.svit.prakarsh

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_account.*

/**
 * Created by itsarjunsinh on 1/28/18.
 */

class AccountFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        updateUI(mAuth.currentUser)
    }

    override fun onResume() {
        super.onResume()

        mAuth = FirebaseAuth.getInstance()
        updateUI(mAuth.currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {

        account_btn_login.visibility = View.GONE
        account_btn_logout.visibility = View.GONE
        account_txt_message.visibility = View.GONE

        account_img_user.setDefaultImageResId(R.drawable.ic_person_black)

        Log.d(javaClass.name,"UID: ${user?.uid}")
        if(user != null){

            account_txt_message.visibility = View.VISIBLE
            account_btn_logout.visibility = View.VISIBLE

            account_btn_logout.setOnClickListener{

                val alertDialog = AlertDialog.Builder(context)
                with(alertDialog) {
                    setTitle("Log Out")
                    setMessage("Are you sure?")

                    setPositiveButton("Yes") { _, _ ->
                        Log.d(javaClass.name, "Trying to logout")
                        mAuth.signOut()
                    }
                    setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                }

            }

        } else {

            account_btn_login.visibility = View.VISIBLE
            account_btn_login.setOnClickListener{
                val intent = Intent(context?.applicationContext, SignInActivity::class.java)
                startActivity(intent)
            }

        }

    }
}