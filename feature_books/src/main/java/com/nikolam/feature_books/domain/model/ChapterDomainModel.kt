package com.nikolam.feature_books.domain.model

import java.io.File

data class ChapterDomainModel(val file : File, val duration : Long, val chapterName : String, val author : String)