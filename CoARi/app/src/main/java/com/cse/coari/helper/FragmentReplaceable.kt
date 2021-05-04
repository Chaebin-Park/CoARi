package com.cse.coari.helper

import java.io.Serializable

interface FragmentReplaceable : Serializable {
    public fun replaceFragment(fragmentId : Int)
}