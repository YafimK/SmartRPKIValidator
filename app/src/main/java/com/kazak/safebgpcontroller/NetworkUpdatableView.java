package com.kazak.safebgpcontroller;

import org.json.JSONObject;

/**
 * Created by fimka on 21/09/2017.
 */

interface NetworkUpdatableView {
     public void updateViewOnResponse(JSONObject jsonResponse);
}
