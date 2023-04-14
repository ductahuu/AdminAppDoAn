package com.example.tahuuduc_duan1_admin.interface_;

import com.google.firebase.database.DatabaseError;

public interface IAfterQuery {
    void onResult(Object obj);
    void onError(DatabaseError error);
}
