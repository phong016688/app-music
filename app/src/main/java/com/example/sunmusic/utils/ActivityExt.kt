package com.example.sunmusic.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.sunmusic.R

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().apply {
        setCustomAnimations(
            R.anim.enter_from_left,
            R.anim.close_to_right,
            R.anim.enter_from_right,
            R.anim.close_to_right
        )
        func()
        commit()
    }
}

fun FragmentActivity.popBackStack() {
    supportFragmentManager.popBackStack()
}

fun FragmentActivity.replaceFragment(
    fragment: Fragment,
    frameId: Int,
    addToStack: Boolean = false,
    clearBackStack: Boolean = false
) {
    supportFragmentManager.inTransaction {
        if (clearBackStack && supportFragmentManager.backStackEntryCount > 0) {
            val first = supportFragmentManager.getBackStackEntryAt(0)
            supportFragmentManager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        if (addToStack) {
            replace(frameId, fragment, fragment.javaClass.simpleName)
                .addToBackStack(fragment.javaClass.simpleName)
        } else {
            replace(frameId, fragment, fragment.javaClass.simpleName)
        }
    }
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, addToStack: Boolean = false) {
    supportFragmentManager.inTransaction {
        if (addToStack) {
            add(frameId, fragment, fragment.javaClass.simpleName)
                .addToBackStack(fragment.javaClass.simpleName)
        } else {
            add(frameId, fragment)
        }
    }
}
