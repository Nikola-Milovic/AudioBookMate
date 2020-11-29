package com.nikolam.feature_folders.folder_chooser

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.nikolam.common.navigation.NavManager
import com.nikolam.feature_folders.FolderManager
import com.nikolam.feature_folders.folder_chooser.data.StorageDirFinder
import com.nikolam.feature_folders.folder_chooser.presenter.FolderChooserViewModel
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


    internal var mockNavManager: NavManager = Mockito.mock(NavManager::class.java)

    internal var dirFinder: StorageDirFinder = Mockito.mock(StorageDirFinder::class.java)

    internal var folderManager: FolderManager = Mockito.mock(FolderManager::class.java)

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
    fun `when successfully loading data, state will be correctly populated`() {
       //setup
//        whenever(folderManager.provideBookCollectionFolders()).thenReturn(setOf("folder1", "folder2"))
//        whenever(folderManager.provideBookSingleFolders()).thenReturn(setOf("folder3"))
        whenever(dirFinder.storageDirs()).thenReturn(listOf(File("dir1"), File("dir2"), File("dir3")))

        // when
        cut.loadData()

        val state = cut.stateLiveData.getOrAwaitValue()

        // then
        state.isError `should be equal to` false
        state.isLoading `should be equal to` false
        state.files `should be equal to` setOf("dir1", "dir2", "dir3")
    }
}