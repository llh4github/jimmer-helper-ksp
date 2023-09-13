# jimmer-helper-ksp
A KSP plugin for assisting the Jimmer framework. 辅助Jimmer框架的KSP插件。
仅适用于`Kotlin`语言
# 用法
需要先引入Jimmer框架相关依赖，如
```kotlin
ksp("org.babyfish.jimmer:jimmer-ksp:${jimmerVersion}")
implementation("org.babyfish.jimmer:jimmer-spring-boot-starter:${jimmerVersion}")
```
再引入本插件：
```kotlin
implementation("io.github.llh4github:jimmer-helper-core:$jimmerHelper")
ksp("io.github.llh4github:jimmer-helper-ksp:$jimmerHelper")
```
## 对Jimmer实体模型的处理
对于模型对象`Author`
```kotlin
@Entity
interface Author : BaseModel {
    @Key
    val firstName: String

    @Key
    val lastName: String

    val gender: Gender

    @ManyToMany(mappedBy = "authors")
    val books: List<Book>

    @Formula(dependencies = ["firstName", "lastName"])
    val fullName: String
        get() = "$firstName $lastName"
}
```
将生成如下代码：
```kotlin
@Suppress("RedundantVisibilityModifier", "Unused")
public data class AuthorInput(
    public var firstName: String? = null,
    public var lastName: String? = null,
    public var gender: Gender? = null,
    public val books: List<BookInput> = emptyList(),
    public override var id: Int? = null,
    public override var createdTime: LocalDateTime? = null,
    public override var updatedBy: Int? = null
) : Input<Author>, BaseModelInput {
    public override fun toEntity(): Author =
        new(Author::class).by {
            this@AuthorInput.firstName?.let { firstName = it }
            this@AuthorInput.lastName?.let { lastName = it }
            this@AuthorInput.gender?.let { gender = it }
            this@AuthorInput.books.forEach { books().addBy { it.toEntity() } }
            this@AuthorInput.id?.let { id = it }
            this@AuthorInput.createdTime?.let { createdTime = it }
            this@AuthorInput.updatedBy?.let { updatedBy = it }
        }
}
```
详细代码参见`example`项目。
除此之外，还会生成简单的`fetcher`类：
```kotlin
@Suppress("RedundantVisibilityModifier", "Unused")
public object AuthorSimpleFetcher {
    public val allScalarFields: Fetcher<Author> = newFetcher(Author::class).by { allScalarFields() }

    public val allTableFields: Fetcher<Author> = newFetcher(Author::class).by { allTableFields() }

    public val allKeyProperties: Fetcher<Author> =
        newFetcher(Author::class).by {
            firstName()
            lastName()
        }
}
```

## 对于自定义对象处理
对于自定义对象需要使用本插件项目内的注解修饰：
```kotlin
@ToJimmerEntity(Person::class, ignoreFields = ["a", "b", "c", "pets"])
data class PersonUpdateDto(
    override val id: Long,
    val name: String,
    val age: Int?,

    @ToJimmerEntityField(ignore = true, rename = "dog")
    val pets: List<String>,
) : BaseDto
```
这将生成扩展方法：
```kotlin
@Suppress("RedundantVisibilityModifier", "Unused")
public fun PersonUpdateDto.toJimmerEntityBuilder(): PersonDraft.MapStruct {
    val rs = PersonDraft.MapStruct()
    rs.id(this.id)
    rs.name(this.name)
    this.age?.let { rs.age(it) }
    return rs
}
@Suppress("RedundantVisibilityModifier", "Unused")
public fun PersonUpdateDto.toJimmerEntity(): Person = toJimmerEntityBuilder().build()
```

