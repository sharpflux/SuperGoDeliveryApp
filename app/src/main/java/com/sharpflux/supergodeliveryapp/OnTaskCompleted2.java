package com.sharpflux.supergodeliveryapp;

public interface OnTaskCompleted2 {
    void onTaskCompleted(String... values);
    void onTaskCompletedHolder(String values, OrderListMainAdapter.ProductViewHolder... holder);
}
