package app.tylenol.common.module

import app.tylenol.common.data.FirestoreEventController
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by baghyeongi on 2018. 4. 11..
 */
@Module
class FirebaseModule {

    @Singleton
    @Provides
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFirestoreEventController(firebaseFirestore: FirebaseFirestore) = FirestoreEventController(firebaseFirestore)

}