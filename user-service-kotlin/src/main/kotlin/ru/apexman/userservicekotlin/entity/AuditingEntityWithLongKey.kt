package ru.apexman.userservicekotlin.entity

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import ru.apexman.userservicekotlin.entity.AuditingEntity


@MappedSuperclass
abstract class AuditingEntityWithLongKey(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
): AuditingEntity()