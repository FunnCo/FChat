package org.example.fchat.repository

import org.example.fchat.model.GroupChat
import org.springframework.data.jpa.repository.JpaRepository

interface GroupChatRepository : JpaRepository<GroupChat, Long>