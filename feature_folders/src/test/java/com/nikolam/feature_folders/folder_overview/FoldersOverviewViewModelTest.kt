package com.nikolam.feature_folders.folder_overview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import com.nikolam.common.navigation.NavManager
import com.nikolam.feature_folders.FolderManager
import com.nikolam.feature_folders.folders_overview.presenter.FoldersOverviewViewModel
import com.nikolam.library_test_utils.CoroutineRule
import com.nikolam.library_test_utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.collection.IsIterableContainingInOrder.contains
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class FoldersOverviewViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private var mockNavManager: NavManager = Mockito.mock(NavManager::class.java)

    private var folderManager: FolderManager = Mockito.mock(FolderManager::class.java)

    private lateinit var cut: FoldersOverviewViewModel

    @Before
    fun setUp() {
        cut = FoldersOverviewViewModel(
            folderManager,
            mockNavManager
        )
    }

    @Test
    fun `when load data is called, will populate state with success data`() {
        //given
        whenever(folderManager.provideBookSingleFolders()).thenReturn(setOf("folder1", "folder2"))
        whenever(folderManager.provideBookCollectionFolders()).thenReturn(setOf("folder3"))

        // when
        cut.loadData()
        val state = cut.stateLiveData.getOrAwaitValue()

        // then
        assertThat(state.files, containsInAnyOrder("folder1", "folder2", "folder3"))
        assert(state.isError == false)
        assert(state.isLoading == false)
    }
}