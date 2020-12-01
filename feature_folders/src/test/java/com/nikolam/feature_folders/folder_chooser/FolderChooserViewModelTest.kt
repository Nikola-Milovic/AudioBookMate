package com.nikolam.feature_folders.folder_chooser

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.nikolam.common.navigation.NavManager
import com.nikolam.feature_folders.FolderManager
import com.nikolam.feature_folders.folder_chooser.data.StorageDirFinder
import com.nikolam.feature_folders.folder_chooser.presenter.FolderChooserViewModel
import com.nikolam.feature_folders.folder_chooser.presenter.OperationMode
import com.nikolam.library_test_utils.CoroutineRule
import com.nikolam.library_test_utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.internal.assertSame
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import java.io.File

@RunWith(JUnit4::class)
class FolderChooserViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()


    private var mockNavManager: NavManager = Mockito.mock(NavManager::class.java)

    private var dirFinder: StorageDirFinder = Mockito.mock(StorageDirFinder::class.java)

    private var folderManager: FolderManager = Mockito.mock(FolderManager::class.java)

    private lateinit var cut: FolderChooserViewModel

    @Before
    fun setUp() {
        cut = FolderChooserViewModel(
            dirFinder,
            folderManager,
            mockNavManager
        )
    }

    @Test
    fun `when folder is chosen, will save the folder if not already saved`() {
       //given
        val file = File("par", "child")
        val op = OperationMode.COLLECTION_BOOK


        // when
        cut.loadData()
        cut.setOperationMode(op)
        cut.fileSelected(file)
        cut.fileChosen()

        // then
        verify(folderManager, times(1)).saveSelectedFolder(file.absolutePath, op)
    }

    @Test
    fun `when folder is chosen, will not save the folder if already saved`() {
        //given
        val file = File("par", "child")
        whenever(folderManager.provideBookCollectionFolders()).thenReturn (setOf(file.absolutePath))
        val op = OperationMode.COLLECTION_BOOK

        // when
        cut.loadData()
        cut.setOperationMode(op)
        cut.fileSelected(file)
        cut.fileChosen()

        // then
        verify(folderManager, times(0)).saveSelectedFolder(file.absolutePath, op)
    }

}