package com.sharpflux.supergodeliveryapp;

public interface BackButtonListener {

        public static final boolean EVENT_PROCESSED = true;
        public static final boolean EVENT_IGNORED = false;
        public boolean onBackPressed();

}
