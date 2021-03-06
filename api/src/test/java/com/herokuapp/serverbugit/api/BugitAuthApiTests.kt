package com.herokuapp.serverbugit.api

import com.herokuapp.serverbugit.api.models.projects.AddProject
import com.herokuapp.serverbugit.api.models.projects.AddProjectMember
import com.herokuapp.serverbugit.api.models.projects.MakeProjectMemberAdmin
import com.herokuapp.serverbugit.api.models.tasks.AddComment
import com.herokuapp.serverbugit.api.models.tasks.AddTask
import com.herokuapp.serverbugit.api.models.tasks.AssignTask
import com.herokuapp.serverbugit.api.models.users.UserCheckPassword
import com.herokuapp.serverbugit.api.models.users.UserUpdateFname
import com.herokuapp.serverbugit.api.models.users.UserUpdateLname
import com.herokuapp.serverbugit.api.models.users.UserUpdatePassword
import com.herokuapp.serverbugit.api.models.workspaces.AddWorkspace
import com.herokuapp.serverbugit.api.models.workspaces.AddWorkspaceMember
import com.herokuapp.serverbugit.api.models.workspaces.AddWorkspaceMemberRequest
import com.herokuapp.serverbugit.api.models.workspaces.MakeUserAdmin
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*
import java.util.*

class BugitAuthApiTests {
    val api = BugitClient.getAuthApiInstance()

    // TODO Workspaces

    @Test
    fun `Add Workspace`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        val userId = "3d21cef2-84ae-48f4-96d5-5b7b6de91351"
        val name = "W 2"
        val desc = "bdvhjbvhjdbshb"
        val createdAt = "2022-03-26"
         runBlocking {
             val res = api.addWorkspace(AddWorkspace(UUID.fromString(userId),name,desc, createdAt))
             assertEquals("success",res.body()?.response)
         }
    }

    @Test
    fun `Add Workspace Member Request`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        val request = AddWorkspaceMemberRequest(UUID.fromString("c439d52d-b5e9-497d-86a2-ede99848f140"),"bruce.wayne330@test.com",
            UUID.fromString("3d21cef2-84ae-48f4-96d5-5b7b6de91351"))
        runBlocking {
            val res = api.addWorkspaceMemberReq(request)
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Add Workspace Member`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.addWorkspaceMember(AddWorkspaceMember(UUID.fromString("63cfc8e4-4789-4aed-983d-5f8563576eb0")))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Workspace Member Exists`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        val request = AddWorkspaceMemberRequest(UUID.fromString("c439d52d-b5e9-497d-86a2-ede99848f140"),"john.snow@abc.com",
            UUID.fromString("3d21cef2-84ae-48f4-96d5-5b7b6de91351"))
        runBlocking {
            val res = api.addWorkspaceMemberReq(request)
            assertEquals("exists",res.body()?.response)
        }
    }

    @Test
    fun `Make User Admin`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.makeUserAdmin(MakeUserAdmin(UUID.fromString("c439d52d-b5e9-497d-86a2-ede99848f140"), UUID.fromString("bcaf8345-ed6c-49af-963e-9138e397180e")))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Get Home`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.getWorkspaces(UUID.fromString("3d21cef2-84ae-48f4-96d5-5b7b6de91351"))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Get Single Workspace`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.getSingleWorkspace(UUID.fromString("c439d52d-b5e9-497d-86a2-ede99848f140"))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Get All Workspace Members`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.getAllWorkspaceMembers(UUID.fromString("c439d52d-b5e9-497d-86a2-ede99848f140"))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Get Workspace Members`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.getWorkspaceMembers(UUID.fromString("c439d52d-b5e9-497d-86a2-ede99848f140"),
                UUID.fromString("7c263e2a-c2a8-4b9d-9562-e687509230b9"))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Delete Workspace`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.deleteWorkspace(UUID.fromString("3aa51949-d3c6-425e-81e6-14afcab8e015"))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Remove Workspace Member`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.removeWorkspaceMember(UUID.fromString("c439d52d-b5e9-497d-86a2-ede99848f140"),
                UUID.fromString("bcaf8345-ed6c-49af-963e-9138e397180e"))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Get Requests`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.getRequests(UUID.fromString("bcaf8345-ed6c-49af-963e-9138e397180e"))
            assertEquals("success",res.body()?.response)
        }
    }

    //TODO Projects

    @Test
    fun `Add Project`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        val project = AddProject(UUID.fromString("c439d52d-b5e9-497d-86a2-ede99848f140"), UUID.fromString("3d21cef2-84ae-48f4-96d5-5b7b6de91351"),"P1",
        "fdghjdjfhh","2022-03-27","Kotlin","2022-04-27")
        runBlocking {
            val res = api.addProject(project)
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Add Project Member`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.addProjectMember(AddProjectMember(UUID.fromString("7c263e2a-c2a8-4b9d-9562-e687509230b9"), UUID.fromString("3d21cef2-84ae-48f4-96d5-5b7b6de91351")))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Make Project Member Admin`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.makeProjectUserAdmin(MakeProjectMemberAdmin(UUID.fromString("7c263e2a-c2a8-4b9d-9562-e687509230b9"), UUID.fromString("3d21cef2-84ae-48f4-96d5-5b7b6de91351")))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Get Single Project`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.getSingleProject(UUID.fromString("7c263e2a-c2a8-4b9d-9562-e687509230b9"))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Get All Project Members`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.getAllProjectMembers(UUID.fromString("7c263e2a-c2a8-4b9d-9562-e687509230b9"))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Get Project Members`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.getProjectMembers(UUID.fromString("9dacf005-2e5a-4f6e-a707-1557805e09fd"),UUID.fromString("eee2662e-3983-4f85-bf79-23f411b88bf1"))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Remove Project Member`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.removeProjectMember(UUID.fromString("7c263e2a-c2a8-4b9d-9562-e687509230b9"),
                UUID.fromString("3d21cef2-84ae-48f4-96d5-5b7b6de91351"))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Delete Project`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.deleteProject(UUID.fromString("7c263e2a-c2a8-4b9d-9562-e687509230b9"))
            assertEquals("success",res.body()?.response)
        }
    }

    //TODO Tasks

    @Test
    fun `Add Task`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        val task = AddTask(UUID.fromString("9dacf005-2e5a-4f6e-a707-1557805e09fd"),"Task 1","dhbcvdshjc fdfsfs fdsfds",
            UUID.fromString("3d21cef2-84ae-48f4-96d5-5b7b6de91351"),"2022-03-27","2022-04-10","Java"
        )
        runBlocking {
            val res = api.addTask(task)
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Assign Task`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        val user = AssignTask(UUID.fromString("eee2662e-3983-4f85-bf79-23f411b88bf1"), UUID.fromString("3d7eee0c-fad4-4e18-8442-64b197dd4f79"))
        runBlocking {
            val res = api.assignTask(user)
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Add Comment`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        val comment = AddComment(UUID.fromString("eee2662e-3983-4f85-bf79-23f411b88bf1"), UUID.fromString("3d7eee0c-fad4-4e18-8442-64b197dd4f79"),
            "This is my comment","2022-03-27"
        )
        runBlocking {
            val res = api.addComment(comment)
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Get Single Task`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.getSingleTask(UUID.fromString("eee2662e-3983-4f85-bf79-23f411b88bf1"))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Delete Task`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.deleteTask(UUID.fromString("eee2662e-3983-4f85-bf79-23f411b88bf1"))
            assertEquals("success",res.body()?.response)
        }
    }

    //TODO Users

    @Test
    fun `Get User`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.getUser(UUID.fromString("3d7eee0c-fad4-4e18-8442-64b197dd4f79"))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Get User Id`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.getUserId()
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Check Password`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.checkPassword(UserCheckPassword(UUID.fromString("3d21cef2-84ae-48f4-96d5-5b7b6de91351"),"random@1234"))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Change First Name`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.changeFname(UserUpdateFname(UUID.fromString("3d21cef2-84ae-48f4-96d5-5b7b6de91351"),"Ned"))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Change Last Name`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.changeLname(UserUpdateLname(UUID.fromString("3d21cef2-84ae-48f4-96d5-5b7b6de91351"),"Stark"))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Change Password`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.changePwd(UserUpdatePassword(UUID.fromString("3d21cef2-84ae-48f4-96d5-5b7b6de91351"),"random@123"))
            assertEquals("success",res.body()?.response)
        }
    }

    @Test
    fun `Delete User`(){
        BugitClient.authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NjM2NjE0NzcsInVzZXJfaWQiOiIzZDIxY2VmMi04NGFlLTQ4ZjQtOTZkNS01YjdiNmRlOTEzNTEifQ.3oqluK5_SCk2E1L0gBoKpyMroicvlL95u1cmEqi6Ma8"
        runBlocking {
            val res = api.deleteUser(UUID.fromString("bcaf8345-ed6c-49af-963e-9138e397180e"))
            assertEquals("success",res.body()?.response)
        }
    }
}