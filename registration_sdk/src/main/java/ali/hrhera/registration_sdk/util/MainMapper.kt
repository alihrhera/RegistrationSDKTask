package ali.hrhera.registration_sdk.util

interface MainMapper<I, O> {

    fun toModel(input: I): O
    fun toEntity(input: O): I
}