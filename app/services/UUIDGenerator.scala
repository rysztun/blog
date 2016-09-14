package services

import javax.inject.Singleton
import java.util.UUID

abstract class UUIDGenerator() {
  def generate: UUID
}

@Singleton
class SimpleUUIDGenerator extends UUIDGenerator {
  def generate: UUID = UUID.randomUUID()
}
