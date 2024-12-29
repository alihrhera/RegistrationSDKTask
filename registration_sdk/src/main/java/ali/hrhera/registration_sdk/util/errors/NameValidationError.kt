package ali.hrhera.registration_sdk.util.errors

data  class NameValidationError(val msg :String):IllegalArgumentException(msg)