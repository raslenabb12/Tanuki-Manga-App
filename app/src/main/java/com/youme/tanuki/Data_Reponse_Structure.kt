package com.youme.tanuki
//latest manga data structure
data class AniListResponse(
    val data: DataWrapper
)

data class DataWrapper(
    val Page: Page
)

data class Page(
    val media: List<Manga>
)

data class Manga(
    val id: Int,
    val title: Title,
    val coverImage: CoverImage,
    val isAdult: Boolean?,
    val chapters: Int?,
    val status: String,
    val countryOfOrigin :String,
    val updatedAt: Long,

)

data class Title(
    val romaji: String,
    val english: String?
)
data class CoverImage(
    val extraLarge:String,
    val large: String,
    val medium: String
)
// manga details structure
data class MangaDetailResponse(
    val data: MangaDetailData?
)

data class MangaDetailData(
    val Media: MangaDetails?
)

data class MangaDetails(
    val id: Int,
    val title: Title?,
    val description: String?,
    val coverImage: CoverImage?,
    val startDate: Date?,
    val endDate: Date?,
    val status: String?,
    val chapters: Int?,
    val volumes: Int?,
    val genres: List<String>?,
    val tags: List<Tag>?,
    val averageScore: Int?,
    val popularity: Int?,
    val bannerImage:String?=null,
    val favourites: Int?,
    val isAdult: Boolean?,
    val siteUrl: String?,
    val characters: CharacterConnection?,
    val staff: StaffConnection?,
    val recommendations: RecommendationConnection?
)


data class Date(
    val year: Int?,
    val month: Int?,
    val day: Int?
)

data class Tag(
    val name: String?,
    val description: String?
)

data class CharacterConnection(
    val edges: List<CharacterEdge>?
)

data class CharacterEdge(
    val node: CharacterNode?,
    val role: String?
)

data class CharacterNode(
    val name: CharacterName?,
    val image: CharacterImage?
)

data class CharacterName(
    val full: String?
)

data class CharacterImage(
    val medium: String,
    val large: String?
)

data class StaffConnection(
    val edges: List<StaffEdge>?
)

data class StaffEdge(
    val node: StaffNode?,
    val role: String?
)

data class StaffNode(
    val name: StaffName?
)

data class StaffName(
    val full: String?
)

data class RecommendationConnection(
    val nodes: List<RecommendationNode>?
)

data class RecommendationNode(
    val mediaRecommendation: RecommendedMedia?
)

data class RecommendedMedia(
    val id: Int,
    val title: Title?,
    val countryOfOrigin:String,
    val coverImage: CoverImage?
)



