HzGov_QueryStationStatsResult = {}

function HzGov_QueryStationStatsResult.query_station_stats(jsonStr)
    local jsonData, err = json.decode(jsonStr)
    if err then
        return nil, err
    end
    local convertedData = {
        StationStats = nil
    }

    local stationStatsInfo = {
        StationID = jsonData.stationId,
        StartTime = jsonData.startTime,
        EndTime = jsonData.endTime,
        StationElectricity = jsonData.StationElectricity,
        EquipmentStatsInfos = {}
    }

    for _, equipmentStatsInfo in ipairs(jsonData.equipmentStatsInfos) do
        local equipmentStats = {
            EquipmentID = equipmentStatsInfo.equipmentTpid,
            EquipmentElectricity = equipmentStatsInfo.equipmentElectricity,
            ConnectorStatsInfos = {}
        }
        for _, connectorStatsInfo in ipairs(equipmentStatsInfo.connectorStatsInfos) do
            local connectorStats = {
                ConnectorID = connectorStatsInfo.connectorTpid,
                ConnectorElectricity = connectorStatsInfo.connectorElectricity
            }
            table.insert(equipmentStats.ConnectorStatsInfos, connectorStats)
        end
        table.insert(stationStatsInfo.EquipmentStatsInfos, equipmentStats)
    end
    convertedData.StationStats = stationStatsInfo

    return json.encode(convertedData)
end
