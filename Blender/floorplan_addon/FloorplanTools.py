import bpy
from bpy.types import Operator, Panel
from . FloorplanToolsFunctions import *

class FloorplanToolsPanel(Panel):
    bl_idname = "FLOORPLAN_PT_PANEL"
    bl_label = "Floorplan Panel"
    bl_category = "Floorplan"
    bl_space_type = "VIEW_3D"
    bl_region_type = "UI"

    def draw(self, context):
        layout = self.layout

        row = layout.row()
        row.operator('floorplan.align_floors', text="Align Floors")

        row = layout.row()
        row.operator('floorplan.hide_reference_images', text="Hide Refs")
        row.operator('floorplan.show_reference_images', text="Show Refs")

        row = layout.row()
        row.operator('floorplan.hide_room_nodes', text="Hide Rooms")
        row.operator('floorplan.show_room_nodes', text="Show Rooms")

        row = layout.row()
        row.operator('floorplan.create_walls', text="Create Walls")

        row = layout.row()
        row.operator('floorplan.remove_walls', text="Remove Walls")

        row = layout.row()
        row.operator('floorplan.create_node', text="Create Node")

        row = layout.row()
        row.operator('floorplan.connect_nodes_to_rooms', text="Connect Nodes To Rooms")

        row = layout.row()
        row.operator('floorplan.connect_rooms_to_nodes', text="Connect Rooms To Nodes")

class AlignFloors(Operator):
    bl_idname = "floorplan.align_floors"
    bl_label = "Align Floors"
    bl_description = "Align two floors by selecting two room nodes that are in the same location on both floors."

    def execute(self, context):
        
        return alignFloors()

class HideReferenceImages(Operator):
    bl_idname = "floorplan.hide_reference_images"
    bl_label = "Hide Reference Images"
    bl_description = "Hide all floorplan reference images."

    def execute(self, context):
        return showReferenceImages(False)

class ShowReferenceImages(Operator):
    bl_idname = "floorplan.show_reference_images"
    bl_label = "Show Reference Images"
    bl_description = "Show all floorplan reference images."

    def execute(self, context):
        return showReferenceImages(True)

class HideRoomNodes(Operator):
    bl_idname = "floorplan.hide_room_nodes"
    bl_label = "Hide Room Nodes"
    bl_description = "Hide all room nodes."

    def execute(self, context):
        return showRoomNodes(False)

class ShowRoomNodes(Operator):
    bl_idname = "floorplan.show_room_nodes"
    bl_label = "Show Room Nodes"
    bl_description = "Show all room nodes."

    def execute(self, context):
        return showRoomNodes(True)

class CreateWalls(Operator):
    bl_idname = "floorplan.create_walls"
    bl_label = "Create Walls"
    bl_description = "Create walls for floors."

    def execute(self, context):
        return createWalls()

class RemoveWalls(Operator):
    bl_idname = "floorplan.remove_walls"
    bl_label = "Remove Walls"
    bl_description = "Remove walls for floors."

    def execute(self, context):
        return removeWalls()

class CreateNode(Operator):
    bl_idname = "floorplan.create_node"
    bl_label = "Create a node."
    bl_description = "Create a new node in the network."

    def execute(self, context):
        return createNode()

class ConnectNodesToRooms(Operator):
    bl_idname = "floorplan.connect_nodes_to_rooms"
    bl_label = "Connect nodes to rooms."
    bl_description = "Connect nodes that are close to a room to that room."

    def execute(self, context):
        return connectRoomNodes(False)

class ConnectRoomsToNodes(Operator):
    bl_idname = "floorplan.connect_rooms_to_nodes"
    bl_label = "Connect rooms to nodes.."
    bl_description = "Connect each unconnected room node to the closest node in the existing node network."

    def execute(self, context):
        return connectRoomNodes(True)