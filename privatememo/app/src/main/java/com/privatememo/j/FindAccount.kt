package com.privatememo.j

import android.animation.ObjectAnimator
import android.app.Dialog
import android.graphics.Point
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.privatememo.j.databinding.FindaccountBinding
import com.privatememo.j.utility.Utility
import com.privatememo.j.viewmodel.FindAccountViewModel


class FindAccount : AppCompatActivity() {

    lateinit var FindBinding: FindaccountBinding
    var findAccountViewModel = FindAccountViewModel()

    lateinit var OneStepDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        val width = size.x
        val height = size.y

        findAccountViewModel = ViewModelProvider(this).get(FindAccountViewModel::class.java)
        FindBinding = DataBindingUtil.setContentView(this, R.layout.findaccount)
        FindBinding.setLifecycleOwner(this)
        FindBinding.findAccountViewModel = findAccountViewModel

        OneStepDialog = Utility.NormalDialogSetting(FindBinding.root.context, R.layout.onestep1dialog, 800)

        var stepOne = Observer<Boolean> { result ->
            if(result == true){
                OneStepDialog.show();

                OneStepDialog.findViewById<TextView>(R.id.check).setOnClickListener {
                    OneStepDialog.dismiss()

                    var objectAnimator1: ObjectAnimator =
                        ObjectAnimator.ofFloat(FindBinding.root, "translationX",-width.toFloat())
                    objectAnimator1.duration = 300
                    objectAnimator1.start()

                    /*var objectAnimator2: ObjectAnimator =
                        ObjectAnimator.ofFloat(FindBinding.twoStep, "translationY",(-width/2).toFloat())
                    objectAnimator2.duration = 300
                    objectAnimator2.start()*/
                }
            }
            else if(result == false){

            }
        }
        findAccountViewModel?.stepOne?.observe(FindBinding.lifecycleOwner!!, stepOne)



    }
}