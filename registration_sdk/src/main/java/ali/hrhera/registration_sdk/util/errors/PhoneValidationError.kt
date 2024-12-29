package ali.hrhera.registration_sdk.util.errors

data  class PhoneValidationError(val msg :String):IllegalArgumentException(msg)