require 'sequel'
require 'csv'

input = File.read ARGV[0]
messages = CSV.parse(input, headers: true)

row_hash = messages[0].to_h

lower_row = {}

row_hash.each_pair do |k, v|
  lower_row[k.downcase] = v
end

lower_row.delete('payloadbinarycontent')
lower_row['action'] ||= 'test'
lower_row['encryption'] ||= 'none'
lower_row['localfilename'] ||= 'v2message.txt'
lower_row['service'] ||= 'service'

DB = Sequel.connect(adapter: 'postgres', host: 'localhost', database: 'sdp_db', user: 'sdp_dbq', password: 'bob')
DB[:message_inq].insert(lower_row)
