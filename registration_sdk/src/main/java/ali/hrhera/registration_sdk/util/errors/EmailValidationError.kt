package ali.hrhera.registration_sdk.util.errors

data  class EmailValidationError(val msg :String):IllegalArgumentException(msg)